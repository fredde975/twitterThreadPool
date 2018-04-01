package com.example.twitter.models;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import java.util.concurrent.Callable;

public class TwitterRequest implements Callable<QueryResult> {
    private Query query;
    private Twitter twitter;


    public TwitterRequest(Query query, Twitter twitter) {
        this.query = query;
        this.twitter = twitter;
    }

    @Override
    public QueryResult call() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread name: " + threadName);
        QueryResult result = twitter.search(query);
        return result;
    }


}
