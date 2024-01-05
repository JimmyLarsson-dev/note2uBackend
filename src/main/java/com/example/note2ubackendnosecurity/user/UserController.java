package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
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
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponse Login(@RequestBody LoginRequest request) throws UserMissingException, CredentialException {
        return service.login(request);
    }

    @PostMapping("/blockUser")
    public String blockUser(@RequestBody BlockRequest request) throws UserMissingException {
        return service.blockUser(request);
    }

    @PutMapping("/unblockUser")
    public String unblockUser(BlockRequest request) throws UserMissingException {
        return service.unblockUser(request);
    }
}
