package com.gaf.reminder.scheduled;

import com.gaf.reminder.bot.Bot;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScheduledTasks {
    private final Bot bot;
    private final Resource resource;

    public ScheduledTasks(Bot bot, ResourceLoader resourceLoader) {
        this.bot = bot;
        resource = resourceLoader.getResource("classpath:tweets.txt");
    }

    @Scheduled(fixedDelay = 200000)
    public void scheduledTweet() throws IOException {
//        InputStream inputStream = resource.getInputStream();
//        InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("Cp1252"));
//        BufferedReader br = new BufferedReader(isr);
//        String line;
//        while ((line = br.readLine()) != null) {
//            bot.tweet(line);
//        }
    }
}
