package com.example.note2ubackendnosecurity.user;

import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.utilities.EntityToDtoConverter;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import com.example.note2ubackendnosecurity.utilities.WelcomeNote;
import com.example.note2ubackendnosecurity.user.DTOs.*;
import org.springframework.stereotype.Service;

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

    //vad händer om man skickar in samma user flera gånger?
    public String blockUser(BlockRequest request) throws UserMissingException {
        UserEntity user = verifyUserInput.checkUsersExistAndReturnCallingUser(request);
        user.getBlockedUsers().add(repo.findByEmail(request.getBlockedUserEmail()).get());
        repo.save(user);
        return "User with email " + request.getBlockedUserEmail() + " has been blocked.";
    }

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
