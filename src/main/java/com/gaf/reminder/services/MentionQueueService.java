package com.gaf.reminder.services;

import com.gaf.reminder.models.ReminderInstruction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MentionQueueService {

    private final RabbitTemplate rabbitTemplate;

    public MentionQueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void pushToQueue(ReminderInstruction reminderInstruction) {

    }
}
