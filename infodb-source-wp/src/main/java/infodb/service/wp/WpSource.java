package infodb.service.wp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import infodb.api.Article;

import java.util.ArrayList;
import java.util.List;

public final class WpSource implements infodb.api.ArticleSource {
    @Override
    public List<Article> getLatest(int num) {
        return mapArticles(WpAdapter.getLatest(num));
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
