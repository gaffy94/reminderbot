package com.gaf.reminder.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Service
@Slf4j
public class BotImpl implements Bot {

    private final Twitter twitter;

    public BotImpl(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public void tweet(String tweet) {
        try {
            log.info("Sending tweet {}", tweet);
            Status status = twitter.updateStatus(tweet);
            log.info("Tweet came back with id {}", status.getId());
        } catch (TwitterException e) {
            log.error(e.getErrorMessage(), e);
        }
    }

    @Override
    public void tweetAt(String tweet, String... users) {
        try {
            String str = String.join("\n", users);
            tweet = tweet.concat("\n").concat(str);
            log.info("Sending tweet {}", tweet);
            Status status = twitter.updateStatus(tweet);
            log.info("Tweet came back with id {}", status.getId());
        } catch (TwitterException e) {
            log.error(e.getErrorMessage(), e);
        }
    }

    @Override
    public void quoteTweet() {

    }

    @Override
    public void sendDm() {

    }

    @Override
    public void deleteTweet() {

    }

    @Override
    public void follow() {

    }

    @Override
    public void Unfollow() {

    }

    @Override
    public void block() {

    }

    @Override
    public void unBlock() {

    }
}
