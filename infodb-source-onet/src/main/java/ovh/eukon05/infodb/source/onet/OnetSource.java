package ovh.eukon05.infodb.source.onet;

import ovh.eukon05.infodb.api.Article;
import ovh.eukon05.infodb.api.ArticleSource;

import java.util.ArrayList;
import java.util.List;

public final class OnetSource implements ArticleSource {
    @Override
    public List<Article> getLatest() {
        List<Article> articles = new ArrayList<>();

        OnetAdapter.getLatest().forEach(element -> articles.add(OnetArticleMapper.mapFromHtml(element, OnetAdapter.getArticleDetails(element.attr("href")))));

        return articles;
    }
}
