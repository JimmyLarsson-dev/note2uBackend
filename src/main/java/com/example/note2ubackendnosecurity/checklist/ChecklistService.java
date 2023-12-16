package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.exceptions.InvalidInputException;
import com.example.note2ubackendnosecurity.other.UserMissingException;
import com.example.note2ubackendnosecurity.user.UserEntity;
import com.example.note2ubackendnosecurity.user.UserRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChecklistService {

    UserRepo repo;

    public ChecklistResponse createChecklist(CreateChecklistRequest request) throws UserMissingException {
        Optional<UserEntity> optUser = repo.findById(UUID.fromString(request.getUserId()));

        if(optUser.isEmpty()) {
            throw new UserMissingException("No such user");
        }

        ChecklistEntity checklistEntity = new ChecklistEntity(
                request.getTitle(),
                request.getItemList(),
                List.of(optUser.get()));
        optUser.get().getCheckLists().add(checklistEntity);
        repo.save(optUser.get());
        return new ChecklistResponse(checklistEntity.getId().toString(), checklistEntity.getTitle());
    }

    public ChecklistResponse updateChecklist(UpdateChecklistRequest request) throws UserMissingException {
        Optional<UserEntity> optUser = repo.findById(UUID.fromString(request.getUserId()));

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
}
