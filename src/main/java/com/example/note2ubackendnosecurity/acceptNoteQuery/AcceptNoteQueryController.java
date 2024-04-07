package com.example.note2ubackendnosecurity.acceptNoteQuery;

import com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs.AcceptNoteRequest;
import com.example.note2ubackendnosecurity.acceptNoteQuery.DTOs.NoteQueryResponse;
import com.example.note2ubackendnosecurity.exceptions.NoteMissingException;
import com.example.note2ubackendnosecurity.exceptions.UserMissingException;
import com.example.note2ubackendnosecurity.notes.DTOs.GetNoteResponse;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/acceptNote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AcceptNoteQueryController {

    private final AcceptNoteQueryService acceptNoteQueryService;

    public AcceptNoteQueryController(AcceptNoteQueryService acceptNoteQueryService) {
        this.acceptNoteQueryService = acceptNoteQueryService;
    }

    @GetMapping("checkReceivedNote")
    public NoteQueryResponse checkReceivedNotes(@RequestHeader String userId) throws NoteMissingException, UserMissingException {
        return acceptNoteQueryService.checkReceivedNotes(userId);
    }

    @PostMapping("/acceptNote")
    public GetNoteResponse acceptNote(@RequestBody AcceptNoteRequest acceptNoteRequest) throws UserMissingException {
        return acceptNoteQueryService.acceptNote(acceptNoteRequest);
    }
}
