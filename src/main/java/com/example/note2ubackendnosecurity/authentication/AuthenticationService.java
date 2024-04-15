package com.example.note2ubackendnosecurity.authentication;

import com.example.note2ubackendnosecurity.config.JwtService;
import com.example.note2ubackendnosecurity.token.Token;
import com.example.note2ubackendnosecurity.token.TokenType;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterRequest;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterResponse;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final VerifyUserInput verifyUserInput;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;

//    public AuthenticationService(
//            VerifyUserInput verifyUserInput,
//            PasswordEncoder passwordEncoder,
//            JwtService jwtService) {
//        this.verifyUserInput = verifyUserInput;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//    }

    public RegisterResponse register(RegisterRequest request) {

        verifyUserInput.checkIfAlreadyRegistered(request);
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .language(request.getLanguage())
                .build();

        String jwt = jwtService.generateToken(user);
        saveUserToken(user, jwt);
        return RegisterResponse.builder()
                .id(String.valueOf(user.getId()))
                .token(jwt)
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private void saveUserToken(UserEntity user, String jwt) {
        Token token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }

}
