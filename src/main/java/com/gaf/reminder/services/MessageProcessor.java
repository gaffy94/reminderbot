package com.gaf.reminder.services;

import com.gaf.reminder.constants.ReminderConstants;
import com.gaf.reminder.exceptions.UnparseableDateException;
import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.util.HandleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@Slf4j
public class MessageProcessor {

    public ReminderInstruction processMessage(Status status) {
        ReminderInstruction instruction = ReminderInstruction.builder()
                .userId(status.getUser().getId())
                .userHandle(HandleUtil.convertToHandle(status.getUser().getScreenName()))
                .userName(status.getUser().getName())
                .isValid(isValidRequest(status.getText()))
                .build();
        if (instruction.isValid()) {
            extractInstruction(instruction, status);
        }
        return instruction;
    }

    private void extractInstruction(ReminderInstruction instruction, Status status) {
        instruction.setReminderText(extractReminder(status.getText()));
        try {
            instruction.setReminderTime(convertToLocalDateTime(extractReminderTime(status.getText())));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            instruction.setValid(false);
        }

    }

    private String extractReminderTime(String text) {
        return StringUtils.substringAfterLast(text.toUpperCase(), ReminderConstants.COMMAND_PERIOD);

    }

    private String extractReminder(String text) {
        String postCommand =
                StringUtils.substringAfter(text.toUpperCase(Locale.ROOT), ReminderConstants.REMINDER_COMMAND);
        int startQuoteIndex = StringUtils.indexOf(postCommand, "\"");
        int endQuoteIndex = StringUtils.lastIndexOf(postCommand, "\"");
        String reminderText =
                StringUtils.substring(postCommand, startQuoteIndex, endQuoteIndex + 1); // include the last quote
        log.info("Returning extracted Reminder text as {}", reminderText);
        return reminderText;
    }

    private LocalDateTime convertToLocalDateTime(String extractReminderTime) {
        String[] reminderTime = extractReminderTime.trim().split(" ");
        LocalDateTime reminderPeriod;
        switch (reminderTime[1].toUpperCase()) {
        case "MINUTE":
        case "MINUTES":
            reminderPeriod = LocalDateTime.now().plusMinutes(Long.parseLong(reminderTime[0]));
            break;
        case "HOUR":
        case "HOURS":
            reminderPeriod = LocalDateTime.now().plusHours(Long.parseLong(reminderTime[0]));
            break;
        case "DAY":
        case "DAYS":
            reminderPeriod = LocalDateTime.now().plusDays(Long.parseLong(reminderTime[0]));
            break;
        default:
            throw new UnparseableDateException("Could Parse Reminder Period");
        }
        return reminderPeriod;
    }

    private boolean isValidRequest(String text) {
        return StringUtils.containsAny(text.toUpperCase(), ReminderConstants.REMINDER_COMMAND,
                ReminderConstants.COMMAND_PERIOD);
    }
}
