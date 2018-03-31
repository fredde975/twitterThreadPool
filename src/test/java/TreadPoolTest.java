import com.example.twitter.models.WordItem;
import org.junit.Test;

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

    }
}
