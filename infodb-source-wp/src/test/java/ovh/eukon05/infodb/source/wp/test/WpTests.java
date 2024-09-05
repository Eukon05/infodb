package ovh.eukon05.infodb.source.wp.test;

import org.junit.jupiter.api.Test;
import ovh.eukon05.infodb.api.Article;
import ovh.eukon05.infodb.api.ArticleSource;

import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class WpTests {
    private static final Random RANDOM = new Random();
    private final ArticleSource source = ServiceLoader.load(ArticleSource.class).findFirst().orElseThrow();

    @Test
    void should_fetch_articles() {
        int articleNo = getCorrectRandomInt();

        List<Article> result = source.getLatest(articleNo);
        assertEquals(result.size(), articleNo);
    }

    @Test
    void should_not_fetch_zero_articles() {
        assertThrows(IllegalArgumentException.class, () -> source.getLatest(0));
    }

    @Test
    void should_not_fetch_negative_articles() {
        assertThrows(IllegalArgumentException.class, () -> source.getLatest(getNegativeRandomInt()));
    }

    // This test tests the expected behavior or WP's internal API, which does not support fetching more than 75 articles
    @Test
    void should_not_fetch_too_much_articles() {
        assertThrows(IllegalArgumentException.class, () -> source.getLatest(getTooBigRandomInt()));
    }

    // 75 articles is a limit put in place by WP's internal API
    private int getCorrectRandomInt() {
        return RANDOM.nextInt(1, 75);
    }

    private int getNegativeRandomInt() {
        return -getCorrectRandomInt();
    }

    private int getTooBigRandomInt() {
        return RANDOM.nextInt(75, Integer.MAX_VALUE);
    }
}
