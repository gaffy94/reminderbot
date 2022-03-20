package com.gaf.reminder.services;

import com.gaf.reminder.properties.ReminderQueueProperties;
import com.gaf.reminder.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReminderQueueService extends QueueService {

    public ReminderQueueService(KafkaTemplate<Long, String> kafkaTemplate,
            ReminderQueueProperties reminderQueueProperties, JsonUtil jsonUtil) {
        super(jsonUtil, kafkaTemplate, reminderQueueProperties.getQueue());
    }

}
