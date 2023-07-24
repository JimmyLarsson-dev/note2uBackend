package com.example.note2ubackendnosecurity.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestHeader String password, @RequestHeader String email, @RequestBody String username) {
        return service.register(username, password, email);
    }
}
