package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.exceptions.UserAlreadyRegisteredException;
import com.example.note2ubackendnosecurity.exceptions.UserNameAlreadyExistsException;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import com.example.note2ubackendnosecurity.other.WelcomeNote;
import org.springframework.stereotype.Service;
import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public RegisterResponse register(String username, String password, String email) {

        //lägg till regex för att kolla mailen

        if (repo.findByEmail(email).isPresent()) {
            throw new UserAlreadyRegisteredException("Email already registered");
        }
        if (repo.findByUsername(username).isPresent()) {
            throw new UserNameAlreadyExistsException("Username already registered");
        }

        System.out.println("username: " + username + " password: " + password + " email: " + email);

        UserEntity user = new UserEntity(
                email,
                username,
                password,
                List.of(),
                List.of());
        NoteEntity noteEntity = new NoteEntity(WelcomeNote.welcomeLable, WelcomeNote.welcomeContent, user);
        user.setNotes(List.of(noteEntity));
        repo.save(user);

        return new RegisterResponse(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail()
                );
    }

    public LoginResponse login(LoginRequest request) throws CredentialException, UserMissingException {
        Optional<UserEntity> optionalUser = repo.findByUsername(request.getUsername());

        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(request.getPassword())) {
                return new LoginResponse(
                        optionalUser.get().getId().toString(),
                        optionalUser.get().getUsername(),
                        optionalUser.get().getEmail()
                );
            } else {
                throw new CredentialException("Incorrect credentials");
            }
        } else {
            throw new UserMissingException("No such user found in database");
        }

    }
}
