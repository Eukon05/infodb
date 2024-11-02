package ovh.eukon05.infodb.source.wp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.time.Instant;
import java.util.List;

final class WpArticleMapper {
    private WpArticleMapper() {
    }

    static Article mapFromJson(JsonObject articleJson, JsonObject articleDetailsJson, ArticleSourceInfo sourceInfo) {
        String title = articleJson.get("title").getAsString();
        String id = articleJson.get("contentId").getAsString();
        String url = articleJson.get("url").getAsString();
        String imageUrl = articleJson.get("image").getAsString();

        long createdAt = articleDetailsJson.get("created").getAsLong();
        List<String> tags = articleDetailsJson.getAsJsonArray("tags")
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(e -> e.get("slug"))
                .map(JsonElement::getAsString)
                .toList();

        return new Article(id, sourceInfo.name(), title, url, imageUrl, Instant.ofEpochSecond(createdAt), tags);
    }
}
