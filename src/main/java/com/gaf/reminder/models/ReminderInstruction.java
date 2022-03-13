package com.gaf.reminder.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReminderInstruction {
    private long userId;
    private long tweetId;
    private String userName;
    private String userHandle;
    private LocalDateTime reminderTime;
    private String reminderText;
    private boolean isValid;
}
