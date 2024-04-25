package com.example.note2ubackendnosecurity.authentication;

import com.example.note2ubackendnosecurity.config.JwtService;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.notes.NoteRepo;
import com.example.note2ubackendnosecurity.token.Token;
import com.example.note2ubackendnosecurity.token.TokenType;
import com.example.note2ubackendnosecurity.user.DTOs.LoginRequest;
import com.example.note2ubackendnosecurity.user.DTOs.LoginResponse;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterRequest;
import com.example.note2ubackendnosecurity.user.DTOs.RegisterResponse;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.LanguageService;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import com.example.note2ubackendnosecurity.utilities.WelcomeNote;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final VerifyUserInput verifyUserInput;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private final UserRepo userRepo;
    private final LanguageService languageService;
    private final WelcomeNote welcomeNote;
    private final NoteRepo noteRepo;

    public RegisterResponse register(RegisterRequest request) {

        verifyUserInput.checkIfAlreadyRegistered(request);
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .language(languageService.selectLanguage(request.getLanguage()))
                .build();
        userRepo.save(user);
        NoteEntity note = new NoteEntity(
                welcomeNote.getWelcomeLable(user.getLanguage()),
                welcomeNote.getWelcomeContent(user.getLanguage()),
                false,
                List.of(user)
        );
        noteRepo.save(note);
        user.getNotes().add(note);
        userRepo.save(user);
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
        Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }

    public LoginResponse login(LoginRequest request) throws UserMissingException, InvalidInputException {
        verifyUserInput.verifyUsernameExists(request.getUsername());
        UserEntity user = userRepo.findByUsername(request.getUsername()).get();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidInputException("Wrong");
        }
        String jwt = jwtService.generateToken(user);
        return new LoginResponse(
                user.getId().toString(),
                jwt,
                user.getUsername(),
                user.getEmail());
    }
}
