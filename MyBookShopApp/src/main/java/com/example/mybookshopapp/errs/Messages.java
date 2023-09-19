package com.example.mybookshopapp.errs;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

public class Messages {
    private Messages() {
        throw new IllegalStateException("Utility class");
    }

    public static String getMessageForLocale(String messageKey) {
        return ResourceBundle.getBundle("lang.messages", LocaleContextHolder.getLocale())
          .getString(messageKey);
    }


}