package ovh.eukon05.infodb.source.donald;

import ovh.eukon05.infodb.api.Article;
import ovh.eukon05.infodb.api.ArticleSource;

import java.util.List;

public class DonaldSource implements ArticleSource {
    @Override
    public List<Article> getLatest() {
        return DonaldAdapter
                .getLatestIds()
                .stream()
                .map(DonaldAdapter::getArticleDetails)
                .map(DonaldArticleMapper::mapFromJson).toList();
    }
}
