package com.gaf.reminder.util;

import org.apache.commons.lang3.StringUtils;

public class HandleUtil {

    public static String convertToHandle(String handle) {
        return StringUtils.startsWith(handle, "@") ? handle : "@".concat(handle);
    }

    public static String prepareMessage(String message, String user) {
        return message.replace("{user}", user);
    }
}
