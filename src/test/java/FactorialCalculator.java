import java.util.concurrent.Callable;

public class FactorialCalculator implements Callable<Integer> {
    private int n;

    public FactorialCalculator(int n) {
        this.n = n;
    }

    public Integer call() {
        int result = 1;

        for (int i = 1; i <= n; i++) {
            result = result * i;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return result;
    }
}