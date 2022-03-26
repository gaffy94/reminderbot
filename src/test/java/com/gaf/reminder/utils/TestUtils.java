package com.gaf.reminder.utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class TestUtils {

    @SneakyThrows
    public static void populateTweets(List<String> validTweets, InputStream resource) {
        InputStreamReader isr = new InputStreamReader(resource, Charset.forName("Cp1252"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            validTweets.add(line);
        }
    }
}
