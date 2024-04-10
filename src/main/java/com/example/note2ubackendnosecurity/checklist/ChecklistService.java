package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.CheckUserInput;
import com.example.note2ubackendnosecurity.utilities.EntityToDtoConverter;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChecklistService {

    UserRepo userRepo;
    ChecklistRepo checklistRepo;
    CheckUserInput checkUserInput;
    EntityToDtoConverter entityToDtoConverter;

    public ChecklistService(UserRepo userRepo, ChecklistRepo checklistRepo, CheckUserInput checkUserInput, EntityToDtoConverter entityToDtoConverter) {
        this.userRepo = userRepo;
        this.checklistRepo = checklistRepo;
        this.checkUserInput = checkUserInput;
        this.entityToDtoConverter = entityToDtoConverter;
    }

    public ChecklistResponse createChecklist(CreateChecklistRequest request) throws UserMissingException {
        checkUserInput.checkIfUserExists(request.getUserId());
        UserEntity user = userRepo.findById(UUID.fromString(request.getUserId())).get();

        UserViewedMap userViewedMap = new UserViewedMap(
                UUID.randomUUID(),
                null,
                UUID.fromString(request.getUserId()),
                true);

        List<Item> itemList = new ArrayList<>();
        request.getItemList()
                .forEach(item -> itemList.add(new Item(UUID.randomUUID(), item.getTitle(), convertStringToBoolean(item.getIsDone()))));

        ChecklistEntity checklistEntity = new ChecklistEntity(
                UUID.randomUUID(),
                request.getTitle(),
                itemList,
                List.of(user),
                userViewedMap);

        checklistEntity.getHasBeenViewed().setNoteOrChecklistId(checklistEntity.getId());
        userViewedMap.setNoteOrChecklistId(checklistEntity.getId());
        checklistRepo.save(checklistEntity);

        return new ChecklistResponse(
                checklistEntity.getId().toString(),
                checklistEntity.getTitle(),
                checklistEntity.getItemList(),
                List.of(entityToDtoConverter.convertUserEntityToUserInNoteOrChecklist(user)),
                userViewedMap
                );
    }

    private boolean convertStringToBoolean(String boolString) {
        return boolString.equals("true");
    }

    public ChecklistResponse updateChecklist(UpdateChecklistRequest request) throws UserMissingException, NoteMissingException {
        checkUserInput.checkIfUserExists(request.getUserId());
        checkUserInput.checkIfChecklistExists(request.getChecklistId());

        Optional<UserEntity> optUser = userRepo.findById(UUID.fromString(request.getUserId()));
        ChecklistEntity checklistEntity = checklistRepo.findById(UUID.fromString(request.getChecklistId())).get();

        if(optUser.get().getCheckLists()
                .stream()
                .noneMatch(x -> x.getId().equals(UUID.fromString(request.getChecklistId())))
        ) {
            throw new InvalidInputException("No such Checklist exists on this user");
        }

        if(!request.getTitle().equals(checklistEntity.getTitle()) &&
                !request.getTitle().isEmpty()) {
            checklistEntity.setTitle(request.getTitle());
        }

        //Verifiering behövs kring vad som skickas in i itemList!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        checklistEntity.getItemList()


        checklistRepo.save(checklistEntity);

        return new ChecklistResponse(
                request.getChecklistId(),
                request.getTitle(),
                null,
                checklistEntity.getUsers()
                        .stream()
                        .map(user -> entityToDtoConverter.convertUserEntityToUserInNoteOrChecklist(user))
                        .collect(Collectors.toList()),
                new UserViewedMap(
                        UUID.fromString(request.getUserId()),
                        true));
    }

//    private Item convertItemDtoToItem(ItemDTO itemDTO) {
//        return new Item()
//    }


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

    private Item updateItems(Item itemFromRequest, Item originalItem) {

        if(itemFromRequest.getTitle() != null && !itemFromRequest.getTitle().isEmpty()) {
            originalItem.setTitle(itemFromRequest.getTitle());
        }

        originalItem.setJobIsDone(true);
        return originalItem;
    }
}
