package ovh.eukon05.infodb.source.onet;

import ovh.eukon05.infodb.api.Article;
import ovh.eukon05.infodb.api.ArticleSource;

import java.util.List;

public final class OnetSource implements ArticleSource {
    @Override
    public List<Article> getLatest(int limit) {
        return OnetAdapter.getLatest(limit)
                .stream()
                .map(e -> OnetArticleMapper.mapFromHtml(e, OnetAdapter.getArticleDetails(e.attr("href"))))
                .toList();
    }
}
