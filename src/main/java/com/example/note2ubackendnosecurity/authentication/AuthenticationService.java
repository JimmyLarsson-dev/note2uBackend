package com.example.note2ubackendnosecurity.authentication;

import com.example.note2ubackendnosecurity.config.JwtService;
import com.example.note2ubackendnosecurity.token.Token;
import com.example.note2ubackendnosecurity.token.TokenType;
import com.example.note2ubackendnosecurity.user.DTOs.LoginRequest;
import com.example.note2ubackendnosecurity.user.DTOs.LoginResponse;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterRequest;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterResponse;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final VerifyUserInput verifyUserInput;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;

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

        userRepo.save(user);
        String jwt = jwtService.generateToken(user);
        saveUserToken(user, jwt);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

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

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepo.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        return new LoginResponse(
                user.getId().toString(),
                jwt,
                user.getUsername(),
                user.getEmail());
    }
}
