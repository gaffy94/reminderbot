package com.gaf.reminder.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app.queue.reminder")
@Getter
@Setter
@AllArgsConstructor
public class ReminderQueueProperties {
    private String queue;
    private String group;

}
