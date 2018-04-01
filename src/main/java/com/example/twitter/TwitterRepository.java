package com.example.twitter;

import com.example.twitter.exceptions.TimeExceededException;
import com.example.twitter.models.TwitterRequest;
import com.example.twitter.models.WordItem;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class TwitterRepository {
    private static final org.apache.log4j.Logger LOG = Logger.getLogger(TwitterRepository.class);
    private static final int MAX_QUERIES = 5;

    private static ApplicationContext context = null;
    private static ExecutorService executorService;

    public TwitterRepository() {
        if (context == null) {
            context = new AnnotationConfigApplicationContext(Configuration.class);
            executorService = (ExecutorService) context.getBean("ConcurrentExecutorService");
        }
    }

    public Map<String, WordItem> getTweets(Query query, Twitter twitter) throws TimeExceededException {
        Map<String, WordItem> resultMap = new HashMap<>();
        long maxID = -1;
        int totalTweets = 0;

        //	This is the loop that retrieve multiple blocks of tweets from Twitter
        for (int queryNumber = 0; queryNumber < MAX_QUERIES; queryNumber++) {
            //	If maxID is -1, then this is our first call and we do not want to tell Twitter what the maximum
            //	tweet id is we want to retrieve.  But if it is not -1, then it represents the lowest tweet ID
            //	we've seen, so we want to start at it-1 (if we start at maxID, we would see the lowest tweet
            //	a second time...
            if (maxID != -1) {
                query.setMaxId(maxID - 1);
            }

            QueryResult r = getQueryResult(query, twitter);
            if (r.getTweets().size() == 0) {
                break;            // Nothing? We must be done
            }


            //	loop through all the tweets and process them.  In this sample program, we just print them
            //	out, but in a real application you might save them to a database, a CSV file, do some
            //	analysis on them, whatever...
            for (Status s : r.getTweets()){
                //	Increment our TWEETS_PER_QUERY of tweets retrieved
                totalTweets++;

                //	Keep track of the lowest tweet ID.  If you do not do this, you cannot retrieve multiple
                //	blocks of tweets...
                if (maxID == -1 || s.getId() < maxID) {
                    maxID = s.getId();
                }

                handleTweetText(s, resultMap);
            }
        }


        LOG.info(String.format("\n\nA total of %d tweets retrieved\n", totalTweets));
        return resultMap;
    }

    private QueryResult getQueryResult(Query query, Twitter twitter) throws TimeExceededException {
        Future<QueryResult> queryResult = executorService.submit(new TwitterRequest(query, twitter));
        QueryResult r = null;
        try {
            r = queryResult.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new TimeExceededException("No response from Twitter in less than 1 second");
        }
        return r;
    }

    private void handleTweetText(Status status, Map<String, WordItem> resultMap) {
        String text = status.getText();
        Arrays.asList(text.split("\\s+"))       //split text on white space
                .stream()
                .forEach(word -> addWordToMap(word, resultMap));
    }


    private void addWordToMap(String word, Map<String, WordItem> resultMap) {
        if (resultMap.containsKey(word)) {
            WordItem item = resultMap.get(word);
            resultMap.replace(word, new WordItem(word, item.getCount() + 1));
        } else {
            resultMap.put(word, new WordItem(word, 1));
        }
    }

}
