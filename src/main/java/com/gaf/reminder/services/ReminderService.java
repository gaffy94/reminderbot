package com.gaf.reminder.services;

import com.gaf.reminder.bot.Bot;
import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.properties.BotMessages;
import com.gaf.reminder.util.HandleUtil;
import com.gaf.reminder.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ReminderService {

    private final MessageProcessor messageProcessor;
    private final Bot bot;
    private final MentionQueueService mentionQueueService;
    private final BotMessages botMessages;
    private final JsonUtil jsonUtil;

    public ReminderService(MessageProcessor messageProcessor, Bot bot,
            MentionQueueService mentionQueueService, BotMessages botMessages, JsonUtil jsonUtil) {
        this.messageProcessor = messageProcessor;
        this.bot = bot;
        this.mentionQueueService = mentionQueueService;
        this.botMessages = botMessages;
        this.jsonUtil = jsonUtil;
    }

    public void processMention(Status status) {
        ReminderInstruction reminderInstruction = messageProcessor.processMessage(status);
        if (reminderInstruction.isValid()) {
            mentionQueueService.pushToQueue(reminderInstruction);
            bot.tweetAt(
                    HandleUtil.prepareMessageTime(
                            HandleUtil.prepareMessageUser(botMessages.getReminderCreateSuccess(),
                                    reminderInstruction.getUserName()), reminderInstruction.getReminderTime())
                    , HandleUtil.convertToHandle(reminderInstruction.getUserHandle()));
        } else {
            bot.tweetAt(
                    HandleUtil.prepareMessageTime(
                            HandleUtil.prepareMessageUser(botMessages.getReminderCreateFailure(),
                                    reminderInstruction.getUserName()), LocalDateTime.now())
                    , HandleUtil.convertToHandle(reminderInstruction.getUserHandle()));
        }

    }

    @KafkaListener(topics = "${app.mention.queue}", groupId = "${app.mention.group}")
    void processReminderMessage(@Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, Acknowledgment acknowledgment) {
        log.info("Consumer 1, Received Message {}, part {}", message, partition);

//        acknowledgment.acknowledge();
    }

}
