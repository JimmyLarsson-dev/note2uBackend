package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import com.example.note2ubackendnosecurity.other.WelcomeNote;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String register(String username, String password, String email) {

//        if (repo.findAll()
//                .stream()
//                .anyMatch(x -> x.getEmail().equals(email))) {
//            return "email already exists!";
//        }
//        if (repo.findAll()
//                .stream()
//                .anyMatch(x -> x.getUsername().equals(username))) {
//            return "user name already exists!";
//        }
        System.out.println("username: " + username + " password: " + password + " email: " + email);

        UserEntity user = new UserEntity(
                email,
                username,
                password,
                List.of());
        NoteEntity noteEntity = new NoteEntity(WelcomeNote.welcomeLable, WelcomeNote.welcomeContent, user);
        user.setNotes(List.of(noteEntity));
        repo.save(user);
        return user.getId().toString();
    }

    public String login(LoginRequest request) throws CredentialException, UserMissingException {
        Optional<UserEntity> optionalUser = repo.findByUsername(request.getUsername());

        if(optionalUser.isPresent()) {
            if(optionalUser.get().getPassword().equals(request.getPassword())) {
                return optionalUser.get().getId().toString();
            } else {
                throw new CredentialException("Incorrect credentials");
            }
        } else {
            throw new UserMissingException("No such user found in database");
        }

    }
}
