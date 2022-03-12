package com.gaf.reminder.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("bot.messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BotMessages {

    private String reminderCreateSuccess;
    private String reminderCreateFailure;

}
