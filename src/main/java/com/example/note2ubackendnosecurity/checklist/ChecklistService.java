package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.checklist.DTOs.*;
import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.user.DTOs.UserInNoteOrChecklist;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import com.example.note2ubackendnosecurity.utilities.VerifyUserInput;
import com.example.note2ubackendnosecurity.utilities.EntityToDtoConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChecklistService {

    UserRepo userRepo;
    ChecklistRepo checklistRepo;
    VerifyUserInput verifyUserInput;
    EntityToDtoConverter entityToDtoConverter;

    public ChecklistService(UserRepo userRepo, ChecklistRepo checklistRepo, VerifyUserInput verifyUserInput, EntityToDtoConverter entityToDtoConverter) {
        this.userRepo = userRepo;
        this.checklistRepo = checklistRepo;
        this.verifyUserInput = verifyUserInput;
        this.entityToDtoConverter = entityToDtoConverter;
    }

    public ChecklistResponse createChecklist(CreateChecklistRequest request) throws UserMissingException {
        verifyUserInput.verifyIfUserExists(request.getUserId());
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


    public ChecklistResponse updateChecklist(UpdateChecklistRequest request) throws UserMissingException, NoteMissingException {
        verifyUserInput.verifyIfUserExists(request.getUserId());
        verifyUserInput.verifyIfChecklistExists(request.getChecklistId());
        UserEntity user = userRepo.findById(UUID.fromString(request.getUserId())).get();
        verifyUserInput.verifyChecklistForUser(request.getChecklistId(), user);
        ChecklistEntity checklistEntity = checklistRepo.findById(UUID.fromString(request.getChecklistId())).get();

        if (user.getCheckLists()
                .stream()
                .noneMatch(x -> x.getId().equals(UUID.fromString(request.getChecklistId())))
        ) {
            throw new InvalidInputException("No such Checklist exists on this user");
        }

        if (!request.getTitle().isEmpty()) {
            checklistEntity.setTitle(request.getTitle());
        }

        //Verifiera bättre vad som uppdateras, och hur!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        checklistEntity.getItemList().clear();
        request.getItemList()
                .forEach(x -> checklistEntity.getItemList()
                        .add(convertItemDtoToItem(x)));
//lägg till hasbeenviewed i DTO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        checklistRepo.save(checklistEntity);

        return new ChecklistResponse(
                request.getChecklistId(),
                request.getTitle(),
                checklistEntity.getItemList(),
                checklistEntity.getUsers()
                        .stream()
                        .map(u -> entityToDtoConverter.convertUserEntityToUserInNoteOrChecklist(u))
                        .collect(Collectors.toList()),
                new UserViewedMap(
                        UUID.fromString(request.getUserId()),
                        true));
    }

    private Item convertItemDtoToItem(ItemDTO itemDTO) {
        return new Item(
                itemDTO.getId(),
                itemDTO.getTitle(),
                convertStringToBoolean(itemDTO.getJobIsDone()));
    }

    private ItemDTO convertItemToItemDto(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getTitle(),
                convertBooleanToString(item.isJobIsDone()));
    }

    public GetChecklistResponse getChecklist(GetChecklistRequest request) throws UserMissingException, NoteMissingException {
        verifyUserInput.verifyIfUserExists(request.getUserId());
        UserEntity user = userRepo.findById(UUID.fromString(request.getUserId())).get();
        verifyUserInput.verifyIfChecklistExists(request.getChecklistId());
        verifyUserInput.verifyChecklistForUser(request.getChecklistId(), user);
        ChecklistEntity checklist = checklistRepo.findById(UUID.fromString(request.getChecklistId())).get();

        List<ItemDTO> itemDTOList = checklist.getItemList().stream().map(x -> convertItemToItemDto(x)).toList();
        List<UserInNoteOrChecklist> userDtoList = checklist.getUsers().stream().map(x -> entityToDtoConverter.convertUserEntityToUserInNoteOrChecklist(x)).toList();

        return new GetChecklistResponse(
                checklist.getId().toString(),
                checklist.getTitle(),
                itemDTOList,
                userDtoList
        );
    }

    private boolean convertStringToBoolean(String boolString) {
        return boolString.equals("true");
    }

    private String convertBooleanToString(Boolean bool) {
        return bool ? "true" : "false";
    }
}
