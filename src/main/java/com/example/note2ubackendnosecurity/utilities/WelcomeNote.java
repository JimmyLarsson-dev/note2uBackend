package com.example.note2ubackendnosecurity.utilities;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WelcomeNote {

    public String getWelcomeLable(String language) {

        switch (language) {
            case "swedish" -> {
                return "Välkommen";
            }
            case "german" -> {
                return "Willkommen";
            }
            default -> {
                return "Welcome";
            }
        }
    }

    public String getWelcomeContent(String language) {
        switch (language) {
            case "swedish" -> {
                return "Information om appen på svenska";
            }
            case "german" -> {
                return "Information in deutsch";
            }
            default -> {
                return "Information in english";
            }
        }
    }
}
