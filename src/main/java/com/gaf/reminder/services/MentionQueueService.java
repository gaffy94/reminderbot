package com.gaf.reminder.services;

import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.properties.MentionQueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MentionQueueService {

    private final RabbitTemplate rabbitTemplate;
    private final MentionQueueProperties mentionQueueProperties;

    public MentionQueueService(RabbitTemplate rabbitTemplate,
            MentionQueueProperties mentionQueueProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.mentionQueueProperties = mentionQueueProperties;
    }

    public void pushToQueue(ReminderInstruction reminderInstruction) {
        rabbitTemplate.convertAndSend(mentionQueueProperties.getExchange(), mentionQueueProperties.getRouteKey(),
                reminderInstruction);
    }
}
