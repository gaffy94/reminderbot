package com.gaf.reminder.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gaf.reminder.bot.Bot;
import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.repository.ReminderRepository;
import com.gaf.reminder.stubs.MessagesStub;
import com.gaf.reminder.util.JsonUtil;
import com.gaf.reminder.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.support.Acknowledgment;
import twitter4j.Status;
import twitter4j.User;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReminderServiceTest {
    private ReminderService reminderService;
    private MessageProcessor messageProcessor;
    private Bot bot;
    private MentionQueueService mentionQueueService;
    private ReminderQueueService reminderQueueService;
    private MessagesStub botMessages;
    private JsonUtil jsonUtil;
    private ReminderRepository reminderRepository;
    private List<String> validTweets = new ArrayList<>();
    private List<String> inValidTweets = new ArrayList<>();
    private final InputStream validTweetsResource;
    private final InputStream inValidTweetsResource;
    private Status status;
    private User user;

    public ReminderServiceTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        validTweetsResource = classLoader.getResourceAsStream("validreminders.txt");
        TestUtils.populateTweets(validTweets, validTweetsResource);
        inValidTweetsResource = classLoader.getResourceAsStream("invalidreminders.txt");
        TestUtils.populateTweets(inValidTweets, inValidTweetsResource);
    }

    @BeforeEach
    void setUp() {
        messageProcessor = new MessageProcessor();
        bot = mock(Bot.class);
        mentionQueueService = mock(MentionQueueService.class);
        reminderQueueService = mock(ReminderQueueService.class);
        botMessages = new MessagesStub();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        jsonUtil = new JsonUtil(mapper);
        reminderRepository = mock(ReminderRepository.class);
        status = mock(Status.class);
        user = mock(User.class);
        reminderService =
                new ReminderService(messageProcessor, bot, mentionQueueService, reminderQueueService, botMessages,
                        jsonUtil, reminderRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Process Valid Mentions")
    @RepeatedTest(value = 5)
    void processValidMention(RepetitionInfo repetitionInfo) {
        when(user.getId()).thenReturn(905099828349L);
        when(user.getScreenName()).thenReturn("@Gaffylise");
        when(user.getName()).thenReturn("Gaffar");
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(897435098534L);
        when(status.getText()).thenReturn(validTweets.get(repetitionInfo.getCurrentRepetition() - 1));
        reminderService.processMention(status);
        verify(mentionQueueService, times(1)).pushToQueue(any());

    }

    @DisplayName("Process Invalid Mentions")
    @RepeatedTest(value = 5)
    void processMention(RepetitionInfo repetitionInfo) {
        when(user.getId()).thenReturn(905099828349L);
        when(user.getScreenName()).thenReturn("@Gaffylise");
        when(user.getName()).thenReturn("Gaffar");
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(897435098534L);
        when(status.getText()).thenReturn(inValidTweets.get(repetitionInfo.getCurrentRepetition() - 1));
        reminderService.processMention(status);
        verify(mentionQueueService, never()).pushToQueue(any());

    }

    @Test
    void processReminderMessage() {
        ReminderInstruction instruction = ReminderInstruction.builder()
                .reminderText("Sample Reminder Text")
                .reminderTime(LocalDateTime.now())
                .build();
        Acknowledgment ack = mock(Acknowledgment.class);
        reminderService.processReminderMessage(jsonUtil.toJson(instruction), 1, ack);
        verify(reminderRepository, times(1)).save(any());
        verify(ack, times(1)).acknowledge();
    }

    @Test
    void sendReminders() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getFireTime()).thenReturn(new Date());
        when(reminderRepository.findByIsProcessedAndReminderTimeIsLessThanEqual(eq(false), any())).thenReturn(
                new ArrayList<>());
        reminderService.sendReminders(context);
        verify(reminderRepository, times(1)).findByIsProcessedAndReminderTimeIsLessThanEqual(eq(false), any());
    }

    @Test
    void sendReminderMessage() {
        ReminderInstruction instruction = ReminderInstruction.builder()
                .reminderText("Sample Reminder Text")
                .userHandle("Gaffylise")
                .userName("Gaffar")
                .reminderTime(LocalDateTime.now())
                .build();
        Acknowledgment ack = mock(Acknowledgment.class);
        reminderService.sendReminderMessage(jsonUtil.toJson(instruction), 1, ack);
        verify(bot, times(1)).tweetAt(any(), any());
        verify(ack, times(1)).acknowledge();
    }
}