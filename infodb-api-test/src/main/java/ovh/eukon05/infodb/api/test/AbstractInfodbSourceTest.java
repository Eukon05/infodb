package ovh.eukon05.infodb.api.test;

import org.junit.jupiter.api.Test;
import ovh.eukon05.infodb.api.Article;
import ovh.eukon05.infodb.api.ArticleSource;

import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractInfodbSourceTest {
    protected static final Random RANDOM = new Random();
    protected final ArticleSource source = ServiceLoader.load(ArticleSource.class).findFirst().orElseThrow();

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

    // 250 articles should demonstrate the modules ability to fetch a large number of articles, while not taking forever
    protected int getCorrectRandomInt() {
        return RANDOM.nextInt(1, 250);
    }

    protected int getNegativeRandomInt() {
        return -getCorrectRandomInt();
    }
}
