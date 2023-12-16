package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.other.UserMissingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/checkList")
public class ChecklistController {

    ChecklistService checklistService;

    @PostMapping("/createChecklist")
    public ChecklistResponse createCheckList(@RequestBody CreateChecklistRequest request) throws UserMissingException {

        return checklistService.createChecklist(request);

    }

    @PutMapping("/updateCheckList")
    public ChecklistResponse updateChecklist(@RequestBody UpdateChecklistRequest request) throws UserMissingException {
        return checklistService.updateChecklist(request);
    }

}
