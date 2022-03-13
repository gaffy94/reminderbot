package com.gaf.reminder.bot;

import org.springframework.stereotype.Service;
import twitter4j.Status;

@Service
public interface Bot {

    public void tweet(String tweet);

    public void tweetAt(String tweet, String... users);

    public void quoteTweet(String tweet, Status status);

    public void replyTweet(String tweet, Status status);

    public void sendDm();

    public void deleteTweet();

    public void follow();

    public void Unfollow();

    public void block();

    public void unBlock();
}
