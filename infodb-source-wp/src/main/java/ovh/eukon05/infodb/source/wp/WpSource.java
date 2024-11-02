package ovh.eukon05.infodb.source.wp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.util.ArrayList;
import java.util.List;

public final class WpSource implements ArticleSource {
    private static final ArticleSourceInfo sourceInfo = new ArticleSourceInfo("WP", "https://wiadomosci.wp.pl/");

    @Override
    public List<Article> getLatest(int limit) {
        return mapArticles(WpAdapter.getLatest(limit));
    }

    @Override
    public ArticleSourceInfo getSourceInfo() {
        return sourceInfo;
    }

    private List<Article> mapArticles(JsonArray articles) {
        List<Article> result = new ArrayList<>();

        articles.forEach(articleElem -> {
            JsonObject articleJson = articleElem.getAsJsonObject();
            JsonObject articleDetailsJson = WpAdapter.getArticleDetails(articleJson.get("contentId").getAsString());
            result.add(WpArticleMapper.mapFromJson(articleJson, articleDetailsJson, sourceInfo));
        });

        return result;
    }
}
