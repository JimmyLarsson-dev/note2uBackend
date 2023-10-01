package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.other.UserMissingException;
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

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return service.register(request.getUsername(), request.getPassword(), request.getEmail());
    }

    @GetMapping("/login")
    public String Login(@RequestBody LoginRequest request) throws UserMissingException, CredentialException {
        return service.login(request);

    }

}
