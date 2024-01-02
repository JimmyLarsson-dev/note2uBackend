package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<Item> itemList = new ArrayList<>();
        request.getItemList()
                .forEach(item -> itemList.add(new Item(UUID.randomUUID(), item.getTitle(),item.isDone())));

        ChecklistEntity checklistEntity = new ChecklistEntity(
                request.getTitle(),
                itemList,
                optUser.get(),
                false);
        System.out.println("??????????????????1 " + checklistEntity.getId());
        System.out.println("??????????????????2 " + checklistEntity.getTitle());
//        System.out.println("??????????????????3 " + checklistEntity.getItemList().get(0));
        System.out.println("??????????????????4 " + checklistEntity.getUsers().get(0).getUsername());
        System.out.println("??????????????????5 " + checklistEntity.isStatusBeenViewed());

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
