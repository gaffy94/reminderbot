package com.gaf.reminder.services;

import com.gaf.reminder.bot.Bot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class MessageProcessorTest {
    private Bot bot;

    @BeforeEach
    void setUp() {
        bot = mock(Bot.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @RepeatedTest(value = 10)
    void processMessage(RepetitionInfo repetitionInfo) {

    }
}