package com.gaf.reminder.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app.queue")
@Getter
@Setter
@AllArgsConstructor
public class QueueProperties {
    private int numPartitions;
    private short replicationFactor;
    private String bootStrapAddress;

}
