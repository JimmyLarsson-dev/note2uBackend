package com.example.note2ubackendnosecurity.utilities;

import org.springframework.stereotype.Service;

@Service
public class LanguageService {

    public String selectLanguage(String language) {
        if(language.equalsIgnoreCase("SWEDISH")) {
            return "swedish";
        } else  {
            return "english";
        }
    }
}
