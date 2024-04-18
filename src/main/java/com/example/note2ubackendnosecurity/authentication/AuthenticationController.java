package com.example.note2ubackendnosecurity.authentication;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.user.DTOs.LoginRequest;
import com.example.note2ubackendnosecurity.user.DTOs.LoginResponse;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterRequest;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws UserMissingException, InvalidInputException {
//        return ResponseEntity.ok(authenticationService.login(request)) ;
//    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws UserMissingException, InvalidInputException {
        return authenticationService.login(request) ;
    }

}
