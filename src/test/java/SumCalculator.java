import java.util.concurrent.Callable;

public class SumCalculator implements Callable<Integer> {
    private int n;

    public SumCalculator(int n) {
        this.n = n;
    }

    public Integer call() {
        int sum = 0;

        for (int i = 1; i <= n; i++) {
            sum += i;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return sum;
    }
}