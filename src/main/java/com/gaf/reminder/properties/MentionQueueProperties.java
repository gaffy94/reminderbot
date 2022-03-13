package com.gaf.reminder.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app.mention")
@Getter
@Setter
@AllArgsConstructor
public class MentionQueueProperties {
    private String queue;
    private int numPartitions;
    private short replicationFactor;
    private String bootStrapAddress;

}
