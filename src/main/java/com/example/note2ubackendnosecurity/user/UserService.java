package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.UserAlreadyRegisteredException;
import com.example.note2ubackendnosecurity.exceptions.UserNameAlreadyExistsException;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.other.WelcomeNote;
import org.springframework.stereotype.Service;
import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public RegisterResponse register(RegisterRequest request) {

        //lägg till regex för att kolla mailen

        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyRegisteredException("Email already registered");
        }
        if (repo.findByUsername(request.getUsername()).isPresent()) {
            throw new UserNameAlreadyExistsException("Username already registered");
        }
        if(!request.getLanguage().equals("SWEDISH") && !request.getLanguage().equals("ENGLISH")) {
            throw new InvalidInputException("Unacceptable language option");
        }

        UserEntity user = new UserEntity(
                request.getEmail(),
                request.getUsername(),
                request.getPassword(),
                List.of(),
                List.of(),
                request.getLanguage());
        NoteEntity noteEntity = new NoteEntity(
                WelcomeNote.welcomeLable,
                WelcomeNote.welcomeContent,
                user, false);

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

    public String blockUser(BlockRequest request) throws UserMissingException {
        UserEntity user = checkUsersExist(request);
        user.getBlockedUsers().add(repo.findByEmail(request.getBlockedUserEmail()).get());

        repo.save(user);

        return "User with email " + request.getBlockedUserEmail() + " has been blocked.";
    }

    public String unblockUser(BlockRequest request) throws UserMissingException {
        UserEntity user = checkUsersExist(request);
        user.getBlockedUsers().remove(repo.findByEmail(request.getBlockedUserEmail()).get());

        repo.save(user);

        return "User has been unblocked";
    }

    private UserEntity checkUsersExist(BlockRequest request) throws UserMissingException {
        if(repo.findById(UUID.fromString(request.getCallingUserId())).isEmpty()) {
            throw new UserMissingException("No such user found in database");
        }
        if(repo.findByEmail(request.getBlockedUserEmail()).isEmpty()) {
            throw new UserMissingException("Cannot find user, unable to block.");
        }

        return repo.findById(UUID.fromString(request.getCallingUserId())).get();
    }
}
