package com.gaf.reminder.services;

import com.gaf.reminder.bot.Bot;
import com.gaf.reminder.entities.Reminder;
import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.properties.BotMessages;
import com.gaf.reminder.repository.ReminderRepository;
import com.gaf.reminder.util.HandleUtil;
import com.gaf.reminder.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class ReminderService {

    private final MessageProcessor messageProcessor;
    private final Bot bot;
    private final MentionQueueService mentionQueueService;
    private final ReminderQueueService reminderQueueService;
    private final BotMessages botMessages;
    private final JsonUtil jsonUtil;
    private final ReminderRepository reminderRepository;

    public ReminderService(MessageProcessor messageProcessor, Bot bot,
            MentionQueueService mentionQueueService,
            ReminderQueueService reminderQueueService, BotMessages botMessages,
            JsonUtil jsonUtil,
            ReminderRepository reminderRepository) {
        this.messageProcessor = messageProcessor;
        this.bot = bot;
        this.mentionQueueService = mentionQueueService;
        this.reminderQueueService = reminderQueueService;
        this.botMessages = botMessages;
        this.jsonUtil = jsonUtil;
        this.reminderRepository = reminderRepository;
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

    @KafkaListener(topics = "${app.queue.mention.queue}", groupId = "${app.queue.mention.group}")
    void processReminderMessage(@Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, Acknowledgment acknowledgment) {
        log.info("Consumer 1, Received Message {}, part {}", message, partition);
        ReminderInstruction instruction = (ReminderInstruction) jsonUtil.toObject(message, ReminderInstruction.class);
        reminderRepository.save(toReminder(instruction));
        acknowledgment.acknowledge();
    }

    private Reminder toReminder(ReminderInstruction instruction) {
        return Reminder.builder()
                .reminderName(instruction.getUserName())
                .reminderText(instruction.getReminderText())
                .reminderTime(instruction.getReminderTime().truncatedTo(ChronoUnit.MINUTES))
                .reminderUser(instruction.getUserHandle())
                .userId(instruction.getUserId())
                .tweetId(instruction.getTweetId())
                .build();
    }

    public void sendReminders(JobExecutionContext context) {
        log.info("Fire Time {}", context.getFireTime());
        List<Reminder> reminders =
                reminderRepository.findByIsProcessedAndReminderTimeIsLessThanEqual(false, LocalDateTime.now());
        processReminders(reminders);
    }

    private void processReminders(List<Reminder> reminders) {
        reminders.forEach(reminder -> {
            reminderQueueService.pushToQueue(ReminderInstruction.builder()
                    .tweetId(reminder.getTweetId())
                    .isValid(true)
                    .userName(reminder.getReminderName())
                    .userHandle(reminder.getReminderUser())
                    .userId(reminder.getUserId())
                    .reminderText(reminder.getReminderText())
                    .reminderTime(reminder.getReminderTime())
                    .build());
            reminder.setProcessed(true);
            reminderRepository.save(reminder);
        });

    }

    @KafkaListener(topics = "${app.queue.reminder.queue}", groupId = "${app.queue.reminder.group}")
    void sendReminderMessage(@Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, Acknowledgment acknowledgment) {
        log.info("Reminder Consumer 1, Received Message {}, part {}", message, partition);
        ReminderInstruction instruction = (ReminderInstruction) jsonUtil.toObject(message, ReminderInstruction.class);
        bot.tweetAt(
                HandleUtil.prepareMessageTime(
                        HandleUtil.prepareMessageUser(botMessages.getReminder(),
                                instruction.getUserName(), instruction.getReminderText()),
                        instruction.getReminderTime())
                , HandleUtil.convertToHandle(instruction.getUserHandle()));
        acknowledgment.acknowledge();
    }
}
