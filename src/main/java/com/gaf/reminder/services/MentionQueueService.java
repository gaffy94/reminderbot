package com.gaf.reminder.services;

import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.properties.MentionQueueProperties;
import com.gaf.reminder.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class MentionQueueService {

    private KafkaTemplate<Long, String> kafkaTemplate;
    private final MentionQueueProperties mentionQueueProperties;
    private final JsonUtil jsonUtil;

    public MentionQueueService(KafkaTemplate<Long, String> kafkaTemplate,
            MentionQueueProperties mentionQueueProperties, JsonUtil jsonUtil) {
        this.kafkaTemplate = kafkaTemplate;
        this.mentionQueueProperties = mentionQueueProperties;
        this.jsonUtil = jsonUtil;
    }

    public void pushToQueue(ReminderInstruction reminderInstruction) {
        String message = jsonUtil.toJson(reminderInstruction);
        ListenableFuture<SendResult<Long, String>>
                future =
                kafkaTemplate.send(mentionQueueProperties.getQueue(), reminderInstruction.getTweetId(), message);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                log.info("Sent message=[{}] with offset=[{}}]", message, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}
