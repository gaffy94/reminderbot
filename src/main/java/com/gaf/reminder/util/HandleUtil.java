package com.gaf.reminder.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public class HandleUtil {

    public static String convertToHandle(String handle) {
        return StringUtils.startsWith(handle, "@") ? handle : "@".concat(handle);
    }

    public static String prepareMessageUser(String message, String user) {
        return message.replace("{user}", user);
    }

    public static String prepareMessageTime(String message, LocalDateTime time) {
        return message.replace("{time}", time.toString());
    }
}
