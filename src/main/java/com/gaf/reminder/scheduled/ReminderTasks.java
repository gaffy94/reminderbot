package com.gaf.reminder.scheduled;

import com.gaf.reminder.services.ReminderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

@Service
public class ReminderTasks implements Job {
    private final ReminderService reminderService;

    public ReminderTasks(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        reminderService.sendReminders(context);
    }
}
