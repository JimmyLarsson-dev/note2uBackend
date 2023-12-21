package com.example.note2ubackendnosecurity.checklist;

import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/checklist")
public class ChecklistController {

    ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PostMapping("/createChecklist")
    public ChecklistResponse createCheckList(@RequestBody CreateChecklistRequest request) throws UserMissingException, InterruptedException {

        return checklistService.createChecklist(request);
    }

    @PutMapping("/updateCheckList")
    public ChecklistResponse updateChecklist(@RequestBody UpdateChecklistRequest request) throws UserMissingException {
        return checklistService.updateChecklist(request);
    }

    @GetMapping("/getChecklist")
    public ChecklistEntity getChecklist(@RequestBody GetChecklistRequest request) throws UserMissingException {
        return checklistService.getChecklist(request);
    }

}
