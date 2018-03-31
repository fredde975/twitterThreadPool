import com.example.twitter.models.WordItem;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TreadPoolTest {




    @Test
    public void compareTwoItems() {
        WordItem item1 = new WordItem("justin", 0);
        WordItem item2 = new WordItem("bieber", 1);

        assertThat(item1.compareTo(item2)).isGreaterThan(0);
    }

}
