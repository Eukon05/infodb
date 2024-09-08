package ovh.eukon05.infodb.api;

import java.util.List;

public interface ArticleSource {
    int DEFAULT_LIMIT = Integer.parseInt(System.getProperty("infodb-article-limit", "20"));

    default List<Article> getLatest() {
        return getLatest(DEFAULT_LIMIT);
    }

    List<Article> getLatest(int limit);
}
