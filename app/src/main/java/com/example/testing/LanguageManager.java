package com.example.testing;

public class LanguageManager {
    private static LanguageManager instance;
    private String currentLanguageCode = "vi";  // Mã ngôn ngữ mặc định

    private LanguageManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String getCurrentLanguageCode() {
        return currentLanguageCode;
    }

    public void setCurrentLanguageCode(String languageCode) {
        this.currentLanguageCode = languageCode;
    }
}
