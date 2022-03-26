package com.gaf.reminder.stubs;

import com.gaf.reminder.properties.BotMessages;

public class MessagesStub extends BotMessages {
    @Override
    public String getReminder() {
        return "Hey {user}, here's your reminder {reminder}. \n {time}.";
    }

    @Override
    public String getReminderCreateFailure() {
        return "Hey {user}, I couldn't create your reminder,\n Check my Bio to see an example or DM me with 'Help' to get more info. \n {time}";
    }

    @Override
    public String getReminderCreateSuccess() {
        return "Hey {user}, I created your reminder successfully!. \n I'll remind you at {time}";
    }
}
