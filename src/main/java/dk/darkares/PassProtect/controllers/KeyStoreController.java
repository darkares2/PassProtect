package dk.darkares.PassProtect.controllers;

import dk.darkares.PassProtect.models.KeyStore;
import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.models.User;
import dk.darkares.PassProtect.services.KeyService;
import dk.darkares.PassProtect.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/key")
public class KeyStoreController {
    @Autowired
    private KeyService keyService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public ResponseEntity<List<KeyStore>> getAll() {
        User user = getAuthenticationUser();
        return new ResponseEntity<>(keyService.getAllByUserId(user.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeyStore> createKey(@RequestBody KeyStore keyStore) {
        User user = getAuthenticationUser();
        keyStore.setUserId(user.getId());
        keyStore.setkeyContent(keyService.generateKey());
        keyStore = keyService.createKey(keyStore);
        return new ResponseEntity<>(keyStore, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        User user = getAuthenticationUser();
        keyService.deleteByUserIdAndId(user.getId(), id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    private User getAuthenticationUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByName(auth.getName());
    }
}
