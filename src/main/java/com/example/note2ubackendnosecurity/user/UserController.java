package com.example.note2ubackendnosecurity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    UserService service;

    @PostMapping("/register")
    public String register(@RequestHeader String password, @RequestHeader String email, @RequestBody String username) {
        return service.register(username, password, email);
    }

}
