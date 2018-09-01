package dk.darkares.PassProtect.controllers;

import dk.darkares.PassProtect.models.User;
import dk.darkares.PassProtect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@Controller
public class HomeController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private static class LoginModel {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUserńame(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private Date timestamp;
        private int code;
        private String error;
        private String message;

        public LoginResponse() {}

        public LoginResponse(int code, String error, String message) {
            this.timestamp = new Date();
            this.code = code;
            this.error = error;
            this.message = message;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @RequestMapping(value = "/")
    public String index(Model model, @RequestHeader("Cookie") String cookies) {
        model.addAttribute("allCookies", cookies);
        return "index";
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET})
    public String login() {
        return "index";
    }
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/loginTest", method = { RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> loginPost(@RequestBody LoginModel login) {
        String message = "";
        try {
            String userName = login.getUsername();
            String password = login.getPassword();
            User user = userRepository.findByNameCaseInsensitive(userName);

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            if (auth != null && auth.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                return new ResponseEntity<>(new LoginResponse(200, "", ""), HttpStatus.OK);
            }
        } catch(Exception ex) {
            message = ex.getMessage();
        }
        return new ResponseEntity<>(new LoginResponse(403, "Authentication failed", message), HttpStatus.OK);
    }

    @RequestMapping(value = {"/{path:^(?!api|built|login|loginTest).*}/**"})
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/";
    }
}