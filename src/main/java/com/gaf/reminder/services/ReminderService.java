package com.gaf.reminder.services;

import com.gaf.reminder.bot.Bot;
import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.properties.BotMessages;
import com.gaf.reminder.util.HandleUtil;
import org.springframework.stereotype.Service;
import twitter4j.Status;

@Service
public class ReminderService {

    private final MessageProcessor messageProcessor;
    private final Bot bot;
    private final MentionQueueService mentionQueueService;
    private final BotMessages botMessages;

    public ReminderService(MessageProcessor messageProcessor, Bot bot,
            MentionQueueService mentionQueueService, BotMessages botMessages) {
        this.messageProcessor = messageProcessor;
        this.bot = bot;
        this.mentionQueueService = mentionQueueService;
        this.botMessages = botMessages;
    }

    public void processMention(Status status) {
        ReminderInstruction reminderInstruction = messageProcessor.processMessage(status);
        if (reminderInstruction.isValid()) {
            mentionQueueService.pushToQueue(reminderInstruction);
            bot.tweetAt(HandleUtil.prepareMessage(botMessages.getReminderCreateSuccess(),
                            reminderInstruction.getUserName())
                    , HandleUtil.convertToHandle(reminderInstruction.getUserHandle()));
        } else {
            bot.tweetAt(HandleUtil.prepareMessage(botMessages.getReminderCreateFailure(),
                            reminderInstruction.getUserName())
                    , HandleUtil.convertToHandle(reminderInstruction.getUserHandle()));
        }
    }
}
