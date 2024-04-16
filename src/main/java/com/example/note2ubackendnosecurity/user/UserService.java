package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.notes.NoteEntity;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.utilities.EntityToDtoConverter;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import com.example.note2ubackendnosecurity.utilities.WelcomeNote;
import com.example.note2ubackendnosecurity.user.DTOs.*;
import org.springframework.stereotype.Service;
import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo repo;
    private final WelcomeNote welcomeNote;
    private final VerifyUserInput verifyUserInput;
    private final EntityToDtoConverter entityToDtoConverter;

    public UserService(UserRepo repo, WelcomeNote welcomeNote, VerifyUserInput verifyUserInput, EntityToDtoConverter entityToDtoConverter) {
        this.repo = repo;
        this.welcomeNote = welcomeNote;
        this.verifyUserInput = verifyUserInput;
        this.entityToDtoConverter = entityToDtoConverter;
    }

    ///Remove this method + endpoint!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public RegisterResponse register(RegisterRequest request) {
        //lägg till regex för att kolla mailen? Eller hantera det med Keycloak?
        verifyUserInput.checkIfAlreadyRegistered(request);
        UserEntity user = createUserEntity(request);
        NoteEntity noteEntity = createNoteEntity(user);
        user.setNotes(List.of(noteEntity));
        repo.save(user);
//        return entityToDtoConverter.createRegisterResponse(user);
        return null;
    }

    private NoteEntity createNoteEntity(UserEntity user) {
        return new NoteEntity(
                welcomeNote.getWelcomeLable(user.getLanguage()),
                welcomeNote.getWelcomeContent(user.getLanguage()),
                user,
                false);
    }

    private UserEntity createUserEntity(RegisterRequest request) {

        return UserEntity.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .notes(List.of())
                .checkLists(List.of())
                .language(request.getLanguage())
                .blockedUsers(List.of())
                .build();

//        UserEntity user = new UserEntity(
//                request.getEmail(),
//                request.getUsername(),
//                request.getPassword(),
//                List.of(),
//                List.of(),
//                selectLanguage(request));
    }

    private String selectLanguage(RegisterRequest request) {
        if(request.getLanguage().equalsIgnoreCase("SWEDISH")) {
            return "swedish";
        } else if (request.getLanguage().equalsIgnoreCase("ENGLISH")) {
            return "english";
        } else {
            throw new InvalidInputException("Unacceptable language option");
        }
    }

    ///Remove this method + endpoint!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public LoginResponse login(LoginRequest request) throws CredentialException, UserMissingException {
//        Optional<UserEntity> optionalUser = repo.findByUsername(request.getUsername());
//        if (optionalUser.isPresent()) {
//            if (optionalUser.get().getPassword().equals(request.getPassword())) {
//                return new LoginResponse(
//                        optionalUser.get().getId().toString(),
//                        optionalUser.get().getUsername(),
//                        optionalUser.get().getEmail()
//                );
//            } else {
//                throw new CredentialException("Incorrect credentials");
//            }
//        } else {
//            throw new UserMissingException("No such user found in database");
//        }
        return null;
    }

    //lägg till funktion för att blocka per username!!!!!!!!!!!!!!!!!!!
    //vad händer om man skickar in samma user flera gånger?
    public String blockUser(BlockRequest request) throws UserMissingException {
        UserEntity user = verifyUserInput.checkUsersExistAndReturnCallingUser(request);
        user.getBlockedUsers().add(repo.findByEmail(request.getBlockedUserEmail()).get());
        repo.save(user);
        return "User with email " + request.getBlockedUserEmail() + " has been blocked.";
    }

    //lägg till funktion för att unblocka per username!!!!!!!!!!!!!!!!!!!
    public String unblockUser(BlockRequest request) throws UserMissingException {
        UserEntity user = verifyUserInput.checkUsersExistAndReturnCallingUser(request);
        if(!request.getBlockedUserEmail().isEmpty()) {
            user.getBlockedUsers().remove(repo.findByEmail(request.getBlockedUserEmail()).get());
            repo.save(user);
            return "User with email " + request.getBlockedUserEmail() +  " has been unblocked";
        }
        if(!request.getBlockedUserUsername().isEmpty()) {
            user.getBlockedUsers().remove(repo.findByUsername(request.getBlockedUserUsername()).get());
            repo.save(user);
            return "User " + request.getBlockedUserUsername() +  " has been unblocked";
        }
        return "No user specified";
    }
}
