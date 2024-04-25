package com.example.note2ubackendnosecurity.utilities;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WelcomeNote {

    public String getWelcomeLable(String language) {

        if (language.equals("swedish")) {
            return "Välkommen";
        }
        return "Welcome";
    }

    public String getWelcomeContent(String language) {
        if (language.equals("swedish")) {
            return "Information om appen på svenska";
        }
        return "Information in english";
    }
}
