package com.gaf.reminder.services;

import com.gaf.reminder.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import twitter4j.Status;
import twitter4j.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageProcessorTest {

    private List<String> validTweets = new ArrayList<>();
    private List<String> inValidTweets = new ArrayList<>();
    private final InputStream validTweetsResource;
    private final InputStream inValidTweetsResource;
    private MessageProcessor messageProcessor;
    private Status status;
    private User user;

    MessageProcessorTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        validTweetsResource = classLoader.getResourceAsStream("validreminders.txt");
        TestUtils.populateTweets(validTweets, validTweetsResource);
        inValidTweetsResource = classLoader.getResourceAsStream("invalidreminders.txt");
        TestUtils.populateTweets(inValidTweets, inValidTweetsResource);

    }

    @BeforeEach
    void setUp() {
        messageProcessor = new MessageProcessor();
        status = mock(Status.class);
        user = mock(User.class);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Validate Valid Tweets")
    @RepeatedTest(value = 5)
    void processValidMessage(RepetitionInfo repetitionInfo) {
        when(user.getId()).thenReturn(905099828349L);
        when(user.getScreenName()).thenReturn("@Gaffylise");
        when(user.getName()).thenReturn("Gaffar");
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(897435098534L);
        when(status.getText()).thenReturn(validTweets.get(repetitionInfo.getCurrentRepetition() - 1));
        Assertions.assertTrue(messageProcessor.processMessage(status).isValid());
    }

    @DisplayName("Validate Invalid Tweets")
    @RepeatedTest(value = 5)
    void processInvalidMessage(RepetitionInfo repetitionInfo) {
        when(user.getId()).thenReturn(905099828349L);
        when(user.getScreenName()).thenReturn("@Gaffylise");
        when(user.getName()).thenReturn("Gaffar");
        when(status.getUser()).thenReturn(user);
        when(status.getId()).thenReturn(897435098534L);
        when(status.getText()).thenReturn(inValidTweets.get(repetitionInfo.getCurrentRepetition() - 1));
        Assertions.assertFalse(messageProcessor.processMessage(status).isValid());
    }

}