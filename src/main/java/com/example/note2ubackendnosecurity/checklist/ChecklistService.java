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

        UserViewedMap userViewedMap = new UserViewedMap(
                UUID.randomUUID(),
                UUID.fromString(request.getUserId()),
                true);

        List<Item> itemList = new ArrayList<>();
        request.getItemList()
                .forEach(item -> itemList.add(new Item(UUID.randomUUID(), item.getTitle(),item.isDone())));

        ChecklistEntity checklistEntity = new ChecklistEntity(
                request.getTitle(),
                itemList,
                optUser.get(),
                userViewedMap);

        checklistRepo.save(checklistEntity);

        return new ChecklistResponse(
                checklistEntity.getId().toString(),
                checklistEntity.getTitle(),
                checklistEntity.getItemList(),
                checklistEntity.getUsers(),
                userViewedMap);
    }

    public ChecklistResponse updateChecklist(UpdateChecklistRequest request) throws UserMissingException {
        Optional<UserEntity> optUser = userRepo.findById(UUID.fromString(request.getUserId()));
        Optional<ChecklistEntity> optionalChecklistEntity = checklistRepo.findById(UUID.fromString(request.getChecklistId()));

        if(optUser.isEmpty()) {
            throw new UserMissingException("No such user");
        }
        if(optionalChecklistEntity.isEmpty()) {
            throw new InvalidInputException("Invalid checklist id");
        }


        if(optUser.get().getCheckLists()
                .stream()
                .noneMatch(x -> x.getId().equals(UUID.fromString(request.getChecklistId())))
        ) {
            throw new InvalidInputException("No such Checklist exists on this user");
        }

        return new ChecklistResponse(
                request.getChecklistId(),
                request.getTitle(),
                request.getItemList(),
                optionalChecklistEntity.get().getUsers(),
                new UserViewedMap(
                        UUID.fromString(request.getUserId()),
                        true));
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
