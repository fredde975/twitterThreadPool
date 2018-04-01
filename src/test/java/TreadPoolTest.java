import com.example.twitter.TwitterRequest;
import com.example.twitter.TwitterService;
import org.junit.Test;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TreadPoolTest {


    @Test
    public void compareTwoItems() {
        ExecutorService pool = Executors.newSingleThreadExecutor();

        Runnable task = new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };

        pool.execute(task);

        pool.shutdown();
    }

    @Test
    public void pool2() {
        ExecutorService pool = Executors.newSingleThreadExecutor();

        Callable<Integer> task = new Callable<Integer>() {
            public Integer call() {
                try {
                    // fake computation time
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                return 1000;
            }
        };

        Future<Integer> result = pool.submit(task);

        try {

            Integer returnValue = result.get();

            System.out.println("Return value = " + returnValue);

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        pool.shutdown();
    }

    @Test
    public void pool3(){
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Integer> sumResult = pool.submit(new SumCalculator(100000));
        Future<Integer> factorialResult = pool.submit(new FactorialCalculator(8));


        try {
            Integer sumValue = sumResult.get();

            System.out.println("Sum Value = " + sumValue);

            Integer factorialValue = factorialResult.get();

            System.out.println("Factorial Value = " + factorialValue);

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        pool.shutdown();
    }


    @Test
    public void pool4(){
        Twitter twitter = TwitterService.createTwitterInstance();
        Query query1 = TwitterService.createQuery("#bieber");
        Query query2 = TwitterService.createQuery("#sweden");
        Query query3 = TwitterService.createQuery("#elin");

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<QueryResult> result1 = pool.submit(new TwitterRequest(query1, twitter));
        Future<QueryResult> result2 = pool.submit(new TwitterRequest(query2, twitter));
        Future<QueryResult> result3 = pool.submit(new TwitterRequest(query2, twitter));


        try {
            QueryResult qr1 = result1.get();
            System.out.println(qr1.getTweets());


            QueryResult qr2 = result2.get();
            System.out.println(qr2.getTweets());

            QueryResult qr3 = result3.get();
            System.out.println(qr3.getTweets());

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        pool.shutdown();
    }

}
