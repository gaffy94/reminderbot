package com.gaf.reminder.config;

import com.gaf.reminder.listener.BotStatusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.FilterQuery;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Configuration
@Slf4j
public class Tweet4jConfig {
    @Bean
    public Twitter configTwitter() {
        return new TwitterFactory().getInstance();
    }

    @Bean
    public TwitterStream configureTwitterStream(BotStatusListener listener) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        FilterQuery query = new FilterQuery("@" + twitterStream.getScreenName());
        twitterStream.filter(query);
        return twitterStream;
    }
}
