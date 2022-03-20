package com.gaf.reminder.services;

import com.gaf.reminder.models.ReminderInstruction;
import com.gaf.reminder.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public abstract class QueueService {
    private final JsonUtil jsonUtil;
    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final String queueName;

    protected QueueService(JsonUtil jsonUtil,
            KafkaTemplate<Long, String> kafkaTemplate, String queueName) {
        this.jsonUtil = jsonUtil;
        this.kafkaTemplate = kafkaTemplate;
        this.queueName = queueName;
    }

    public void pushToQueue(ReminderInstruction reminderInstruction) {
        String message = toJson(reminderInstruction);
        ListenableFuture<SendResult<Long, String>>
                future =
                kafkaTemplate.send(queueName, reminderInstruction.getTweetId(), message);
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

    public String toJson(Object object) {
        return jsonUtil.toJson(object);
    }
}
