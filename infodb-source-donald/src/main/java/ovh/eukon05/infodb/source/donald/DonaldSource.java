package ovh.eukon05.infodb.source.donald;

import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.util.List;

public class DonaldSource implements ArticleSource {
    private static final ArticleSourceInfo sourceInfo = new ArticleSourceInfo("DONALDPL", "https://donald.pl/news");

    @Override
    public List<Article> getLatest(int limit) {
        return DonaldAdapter
                .getLatestIds(limit)
                .stream()
                .map(DonaldAdapter::getArticleDetails)
                .map(json -> DonaldArticleMapper.mapFromJson(json, sourceInfo)).toList();
    }

    @Override
    public ArticleSourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
