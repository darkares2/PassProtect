package dk.darkares.PassProtect.controllers;

import dk.darkares.PassProtect.misc.PasswordGenerator;
import dk.darkares.PassProtect.models.KeyStore;
import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.models.User;
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
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(16);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @RequestMapping(value = "/decrypt/{id}", method = {RequestMethod.GET})
    public ResponseEntity<String> decryptById(@PathVariable("id") long id) {
        String decryptedPassword = null;
        User user = getAuthenticationUser();
        Password password = passwordService.getPasswordById(id);
        KeyStore key = keyService.getKeyById(password.getKeyId());
        decryptedPassword = passwordService.decryptPassword(key.getkeyContent(), password.getPassword());
        return new ResponseEntity<>(decryptedPassword, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        User user = getAuthenticationUser();
        passwordService.deleteByUserIdAndId(user.getId(), id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @RequestMapping(value = "/", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Password> createPassword(@RequestBody Password password) throws Exception {
        User user = getAuthenticationUser();
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
