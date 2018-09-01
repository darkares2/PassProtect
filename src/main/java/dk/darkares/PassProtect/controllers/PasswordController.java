package dk.darkares.PassProtect.controllers;

import dk.darkares.PassProtect.models.Password;
import dk.darkares.PassProtect.models.User;
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


    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public ResponseEntity<List<Password>> getAll(@RequestHeader("Cookie") String cookies) {
        User user = getAuthenticationUser();
        return new ResponseEntity<>(passwordService.getAllByUserId(user.getId()), HttpStatus.OK);
    }

    private User getAuthenticationUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByName(auth.getName());
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        User user = getAuthenticationUser();
        passwordService.deleteByUserIdAndId(user.getId(), id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @RequestMapping(value = "/", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Password> createPassword(@RequestBody Password password) {
        User user = getAuthenticationUser();
        password.setUserId(user.getId());
        password = passwordService.createPassword(password);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    public ResponseEntity<Password> getById(@PathVariable("id") long id) {
        User user = getAuthenticationUser();
        return new ResponseEntity<>(passwordService.getByUserIdAndId(user.getId(), id), HttpStatus.OK);
    }
}
