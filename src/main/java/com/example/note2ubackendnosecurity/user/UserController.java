package com.example.note2ubackendnosecurity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping("register")
    public String register(@RequestBody String username, String password, String email) {
        return service.register(username, password, email);
    }

}
