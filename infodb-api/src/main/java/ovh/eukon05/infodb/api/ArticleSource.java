package ovh.eukon05.infodb.api;

import java.util.List;

public interface ArticleSource {
    int defaultLimit = Integer.parseInt(System.getProperty("infodb-article-limit", "20"));

    default List<Article> getLatest() {
        return getLatest(defaultLimit);
    }

    List<Article> getLatest(int limit);
}
