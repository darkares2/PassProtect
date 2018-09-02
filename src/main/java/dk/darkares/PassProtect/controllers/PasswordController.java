package dk.darkares.PassProtect.controllers;

import dk.darkares.PassProtect.misc.PasswordGenerator;
import dk.darkares.PassProtect.models.KeyStore;
import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.models.User;
import dk.darkares.PassProtect.services.EventLogService;
import dk.darkares.PassProtect.services.KeyService;
import dk.darkares.PassProtect.services.PasswordService;
import dk.darkares.PassProtect.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/password")
public class PasswordController {
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserService userService;
    @Autowired
    private KeyService keyService;

    @Autowired
    private EventLogService eventLogService;


    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public ResponseEntity<List<Password>> getAll(@RequestHeader("Cookie") String cookies) {
        User user = getAuthenticationUser();
        return new ResponseEntity<>(passwordService.getAllByUserId(user.getId()), HttpStatus.OK);
    }

    private User getAuthenticationUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByName(auth.getName());
    }

    @RequestMapping(value = "/generate", method = {RequestMethod.GET})
    public ResponseEntity<String> generate() {
        String password = generatePassword();
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    private String generatePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .usePunctuation(true)
                .build();
        return passwordGenerator.generate(16);
    }

    @RequestMapping(value = "/decrypt/{id}", method = {RequestMethod.GET})
    public ResponseEntity<String> decryptById(@PathVariable("id") long id) {
        String decryptedPassword = null;
        User user = getAuthenticationUser();
        Password password = passwordService.getPasswordById(id);
        eventLogService.createEvent("Decrypt", password.getName() + " By user " + user.getName());
        KeyStore key = keyService.getKeyById(password.getKeyId());
        decryptedPassword = passwordService.decryptPassword(key.getkeyContent(), password.getPassword());
        return new ResponseEntity<>(decryptedPassword, HttpStatus.OK);
    }
    @RequestMapping(value = "/regen/{id}", method = {RequestMethod.PUT})
    public ResponseEntity regenerateById(@PathVariable("id") long id) throws Exception {
        User user = getAuthenticationUser();
        Password password = passwordService.getPasswordByUserIdAndId(user.getId(), id);
        eventLogService.createEvent("Regenerate", password.getName() + " By user " + user.getName());
        String newPassword = generatePassword();
        KeyStore key = keyService.getKeyById(password.getKeyId());
        if (key == null)
            throw new Exception("Key not found");
        password.setPassword(passwordService.encryptPassword(key.getkeyContent(), newPassword));
        passwordService.createPassword(password);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        User user = getAuthenticationUser();
        eventLogService.createEvent("Delete", "Item " + id  + " By user " + user.getName());
        passwordService.deleteByUserIdAndId(user.getId(), id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @RequestMapping(value = "/", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Password> createPassword(@RequestBody Password password) throws Exception {
        User user = getAuthenticationUser();
        eventLogService.createEvent("Create", "By user " + user.getName());
        password.setUserId(user.getId());
        KeyStore key = keyService.getKeyById(password.getKeyId());
        if (key == null)
            throw new Exception("Key not found");
        password.setPassword(passwordService.encryptPassword(key.getkeyContent(), password.getPassword()));
        password = passwordService.createPassword(password);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Password> getById(@PathVariable("id") long id) {
        User user = getAuthenticationUser();
        return new ResponseEntity<>(passwordService.getByUserIdAndId(user.getId(), id), HttpStatus.OK);
    }
}
