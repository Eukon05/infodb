package ovh.eukon05.infodb.source.wp.test;

import org.junit.jupiter.api.Test;
import ovh.eukon05.infodb.api.test.AbstractInfodbSourceTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

final class WpTests extends AbstractInfodbSourceTest {
    // This test tests the expected behavior or WP's internal API, which does not support fetching more than 75 articles
    @Test
    void should_not_fetch_too_much_articles() {
        assertThrows(IllegalArgumentException.class, () -> source.getLatest(getTooBigRandomInt()));
    }

    // 75 articles is a limit put in place by WP's internal API
    @Override
    protected int getCorrectRandomInt() {
        return RANDOM.nextInt(1, 75);
    }

    private int getTooBigRandomInt() {
        return RANDOM.nextInt(75, Integer.MAX_VALUE);
    }
}
