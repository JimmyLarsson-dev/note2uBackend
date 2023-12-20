package com.example.note2ubackendnosecurity.exceptions;

public class NoteAccessMissingException extends Exception{
    public NoteAccessMissingException(String message) {
        super(message);
    }
}
