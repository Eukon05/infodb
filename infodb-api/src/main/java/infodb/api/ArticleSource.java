package infodb.api;

import java.util.List;

public interface ArticleSource {
    List<Article> getLatest();
}
