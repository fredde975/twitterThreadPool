package com.example.twitter;

import com.example.twitter.exceptions.TimeExceededException;
import com.example.twitter.models.WordItem;
import com.example.twitter.utils.TwitterUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TwitterService {
    private static final Logger LOG = Logger.getLogger(TwitterService.class);
    private static final String CONSUMER_KEY = "SSVbNkmx7ugzaOfaSsG8iRSij";
    private static final String CONSUMER_SECRET = "fBop27mqeaK8tcYp5hmL7vmSWSfbp3ys4qP14JgWyqMsh5UA6X";
    private static final String ACCESS_TOKEN = "899157129722048512-YHceWk6N45aBm4g4rSrQH3EXxwPjaPG";
    private static final String ACCESS_TOKEN_SECRET = "hNI81vC3XbSp3tpiQ4m1dywL1KbJ0W3O0LqfcDwCsMAqO";
    private static final int TWEETS_PER_QUERY = 100;

//    private static ApplicationContext context = null;
//    private static ExecutorService executorService;


    @Autowired
    TwitterRepository twitterRepository;

//    public TwitterService() {
//        if (context == null) {
//            context = new AnnotationConfigApplicationContext(Configuration.class);
//            executorService = (ExecutorService) context.getBean("ConcurrentExecutorService");
//        }else{
//            System.out.println("context....");
//        }
//    }


    public List<WordItem> handleRequest(String hashTag) throws TwitterException, TimeExceededException {
        Twitter twitter = createTwitterInstance();
        TwitterUtil.checkLimits(twitter);
        Query queryMax = createQuery(hashTag);
        Map<String, WordItem> resultMap = twitterRepository.getTweets(queryMax, twitter);
        List<WordItem> wordItems = createSortedList(resultMap);

        return wordItems;
    }

//    public List<WordItem> getTweets(String hashTag) throws TwitterException, TimeoutException, ExecutionException, InterruptedException {
//        Future<QueryResult> result = executorService.submit( new TwitterRequest(createQuery(hashTag), createTwitterInstance()));
//        List<Status> statusList = result.get(100, TimeUnit.SECONDS).getTweets();
//
//        return statusList.stream().map(s -> new WordItem(s.getText(), 1)).collect(Collectors.toList());
//    }


    public static Twitter createTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    public static Query createQuery(String hashTag) {
        Query queryMax = new Query(hashTag);

        queryMax.setCount(TWEETS_PER_QUERY);
        return queryMax;
    }


    private List<WordItem> createSortedList(Map<String, WordItem> resultMap) {
        List<WordItem> wordItems = resultMap.values().stream()
                .sorted()
                .limit(100)
                .collect(Collectors.toList());

        wordItems.stream().forEach(LOG::info);
        return wordItems;
    }

}
