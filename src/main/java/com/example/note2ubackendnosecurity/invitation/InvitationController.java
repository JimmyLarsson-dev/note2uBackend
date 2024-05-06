package com.example.note2ubackendnosecurity.invitation;

import com.example.note2ubackendnosecurity.invitation.DTOs.InvitationRequest;
import com.example.note2ubackendnosecurity.invitation.DTOs.DeclineNoteRequest;
import com.example.note2ubackendnosecurity.invitation.DTOs.InvitationResponse;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.GetNoteResponse;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/acceptNote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("checkReceivedNote")
    public InvitationResponse checkReceivedNotes(@RequestHeader String userId) throws NoteMissingException, UserMissingException {
        return invitationService.checkReceivedNotes(userId);
    }

    @PostMapping("/acceptNote")
    public GetNoteResponse acceptNote(@RequestBody InvitationRequest invitationRequest) throws UserMissingException {
        return invitationService.acceptNote(invitationRequest);
    }

    @PostMapping("/declineNote")
    public String declineNote(@RequestBody DeclineNoteRequest request) {
        return invitationService.declineNote(request);
    }
}
