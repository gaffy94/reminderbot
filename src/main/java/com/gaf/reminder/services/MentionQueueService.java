package com.gaf.reminder.services;

import com.gaf.reminder.properties.MentionQueueProperties;
import com.gaf.reminder.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MentionQueueService extends QueueService {

    public MentionQueueService(KafkaTemplate<Long, String> kafkaTemplate,
            MentionQueueProperties mentionQueueProperties, JsonUtil jsonUtil) {
        super(jsonUtil, kafkaTemplate, mentionQueueProperties.getQueue());
    }

}
