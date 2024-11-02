package ovh.eukon05.infodb.source.onet;

import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.util.List;

public final class OnetSource implements ArticleSource {
    private static final ArticleSourceInfo sourceInfo = new ArticleSourceInfo("ONET", "https://wiadomosci.onet.pl");

    @Override
    public List<Article> getLatest(int limit) {
        return OnetAdapter.getLatest(limit)
                .stream()
                .map(e -> OnetArticleMapper.mapFromHtml(e, OnetAdapter.getArticleDetails(e.attr("href")), sourceInfo))
                .toList();
    }

    @Override
    public ArticleSourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
