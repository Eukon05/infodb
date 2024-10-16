package ovh.eukon05.infodb.source.wp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.Article;

import java.time.Instant;
import java.util.List;

final class WpArticleMapper {
    private static final String ORIGIN = "WP";

    private WpArticleMapper() {
    }

    static Article mapFromJson(JsonObject articleJson, JsonObject articleDetailsJson) {
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

        return new Article(id, ORIGIN, title, url, imageUrl, Instant.ofEpochSecond(createdAt), tags);
    }
}
