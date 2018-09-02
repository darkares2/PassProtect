package dk.darkares.PassProtect.controllers;

import dk.darkares.PassProtect.models.EventLog;
import dk.darkares.PassProtect.models.KeyStore;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/event")
public class EventLogController {
    @Autowired
    private EventLogService eventLogService;


    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public ResponseEntity<List<EventLog>> getAll() {
        return new ResponseEntity<>(eventLogService.getLatest(), HttpStatus.OK);
    }
}
