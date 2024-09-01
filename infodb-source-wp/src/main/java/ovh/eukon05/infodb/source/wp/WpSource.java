package ovh.eukon05.infodb.source.wp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.Article;
import ovh.eukon05.infodb.api.ArticleSource;

import java.util.ArrayList;
import java.util.List;

public final class WpSource implements ArticleSource {
    @Override
    public List<Article> getLatest(int limit) {
        return mapArticles(WpAdapter.getLatest(limit));
    }

    private List<Article> mapArticles(JsonArray articles) {
        List<Article> result = new ArrayList<>();

        articles.forEach(articleElem -> {
            JsonObject articleJson = articleElem.getAsJsonObject();
            JsonObject articleDetailsJson = WpAdapter.getArticleDetails(articleJson.get("contentId").getAsString());
            result.add(WpArticleMapper.mapFromJson(articleJson, articleDetailsJson));
        });

        return result;
    }
}
