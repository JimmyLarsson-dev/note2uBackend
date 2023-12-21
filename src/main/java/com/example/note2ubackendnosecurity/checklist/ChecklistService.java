package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChecklistService {

    UserRepo userRepo;
    ChecklistRepo checklistRepo;

    public ChecklistService(UserRepo userRepo, ChecklistRepo checklistRepo) {
        this.userRepo = userRepo;
        this.checklistRepo = checklistRepo;
    }

    public ChecklistResponse createChecklist(CreateChecklistRequest request) throws UserMissingException, InterruptedException {
        Optional<UserEntity> optUser = userRepo.findById(UUID.fromString(request.getUserId()));

        if(optUser.isEmpty()) {
            throw new UserMissingException("No such user");
        }

        ChecklistEntity checklistEntity = new ChecklistEntity(
                UUID.randomUUID(),
                request.getTitle(),
                request.getItemList(),
                optUser.get(),
                false);
        System.out.println("??????????????????" + checklistEntity.getId());
        System.out.println("??????????????????" + checklistEntity.getTitle());
        System.out.println("??????????????????" + checklistEntity.getItemList().get(1));
        System.out.println("??????????????????" + checklistEntity.getUsers().get(0).getUsername());
        System.out.println("??????????????????" + checklistEntity.isStatusBeenViewed());



        checklistRepo.save(checklistEntity);
//        optUser.get().getCheckLists().add(checklistEntity);
//        userRepo.save(optUser.get());
        return new ChecklistResponse(checklistEntity.getId().toString(), checklistEntity.getTitle());
    }

    public ChecklistResponse updateChecklist(UpdateChecklistRequest request) throws UserMissingException {
        Optional<UserEntity> optUser = userRepo.findById(UUID.fromString(request.getUserId()));

        if(optUser.isEmpty()) {
            throw new UserMissingException("No such user");
        }

        if(optUser.get().getCheckLists()
                .stream()
                .noneMatch(x -> x.getId().equals(UUID.fromString(request.getChecklistId())))
        ) {
            throw new InvalidInputException("No such Checklist exists on this user");
        }

        return new ChecklistResponse(request.getChecklistId(), request.getTitle());

    }

    public ChecklistEntity getChecklist(GetChecklistRequest request) throws UserMissingException {

        Optional<UserEntity> optUser = userRepo.findById(UUID.fromString(request.getUserId()));

        if(optUser.isEmpty()) {
            throw new UserMissingException("No such user found");
        }

        if(optUser.get().getCheckLists()
                .stream()
                .noneMatch(x -> x.getId().equals(UUID.fromString(request.getChecklistId())))
        ) {
            throw new InvalidInputException("No such checklist for this user");
        }

        return (ChecklistEntity) optUser.get().getCheckLists()
                .stream()
                .filter(x -> x.getId().equals(UUID.fromString(request.getChecklistId())));
    }
}
