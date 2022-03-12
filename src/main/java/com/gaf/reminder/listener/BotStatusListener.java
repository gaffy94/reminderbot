package com.gaf.reminder.listener;

import com.gaf.reminder.services.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

@Service
@Slf4j
public class BotStatusListener implements twitter4j.StatusListener {

    private final ReminderService reminderService;

    public BotStatusListener(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public void onStatus(Status status) {
        log.info("Got message {} from {}", status.getText(), status.getUser().getName());
        reminderService.processMention(status);
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        log.info("Got Deletion Notice on {}, nothing to do for now!", statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        log.info("Got Track Notice Count on {}, System Usage Increasing!", i);
    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {
        log.error(e.getMessage(), e);
    }
}
