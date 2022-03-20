package com.gaf.reminder.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HandleUtil {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm");

    private HandleUtil() {
        // Private Constructor, No Instance Creation Allowed
    }

    public static String convertToHandle(String handle) {
        return StringUtils.startsWith(handle, "@") ? handle : "@".concat(handle);
    }

    public static String prepareMessageUser(String message, String user) {
        return message.replace("{user}", user);
    }

    public static String prepareMessageUser(String message, String user, String reminder) {
        return message.replace("{user}", user).replace("{reminder}", reminder);
    }

    public static String prepareMessageTime(String message, LocalDateTime time) {
        return message.replace("{time}", time.format(format));
    }

}
