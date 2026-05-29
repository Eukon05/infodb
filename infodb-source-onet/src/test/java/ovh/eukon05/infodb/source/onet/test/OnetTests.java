package ovh.eukon05.infodb.source.onet.test;

import org.junit.jupiter.api.Test;
import ovh.eukon05.infodb.api.source.test.AbstractInfodbSourceTest;

import static org.junit.jupiter.api.Assertions.assertThrows;


final class OnetTests extends AbstractInfodbSourceTest {
    // This test tests the expected behavior of ONET's internal API, which does not support fetching more than 99 articles
    @Test
    void should_not_fetch_too_much_articles() {
        assertThrows(IllegalArgumentException.class, () -> source.getLatest(getTooBigRandomInt()));
    }

    // 99 articles is a limit put in place by ONET
    @Override
    protected int getCorrectRandomInt() {
        return RANDOM.nextInt(1, 100);
    }

    private int getTooBigRandomInt() {
        return RANDOM.nextInt(100, Integer.MAX_VALUE);
    }
}
