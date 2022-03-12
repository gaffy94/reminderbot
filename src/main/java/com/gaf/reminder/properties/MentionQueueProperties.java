package com.gaf.reminder.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app.mention")
@Getter
@Setter
public class MentionQueueProperties {
    private String queue;
    private String routeKey;
    private String exchange;

    public MentionQueueProperties(String queue, String routeKey, String exchange) {
        this.queue = queue;
        this.routeKey = routeKey;
        this.exchange = exchange;
    }
}
