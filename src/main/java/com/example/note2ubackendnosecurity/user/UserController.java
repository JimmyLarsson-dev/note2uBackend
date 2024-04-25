package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.user.DTOs.*;
import org.springframework.web.bind.annotation.*;
import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping("/blockUser")
    public String blockUser(@RequestBody BlockRequest request) throws UserMissingException {
        return service.blockUser(request);
    }

    @PostMapping("/unblockUser")
    public String unblockUser(@RequestBody BlockRequest request) throws UserMissingException {
        return service.unblockUser(request);
    }



}
