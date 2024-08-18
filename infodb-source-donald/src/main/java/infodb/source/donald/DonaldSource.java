package infodb.source.donald;

import infodb.api.Article;
import infodb.api.ArticleSource;

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
