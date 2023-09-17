package com.example.note2ubackendnosecurity.other;

public class NoteAccessMissingException extends Exception{
    public NoteAccessMissingException(String message) {
        super(message);
    }
}
