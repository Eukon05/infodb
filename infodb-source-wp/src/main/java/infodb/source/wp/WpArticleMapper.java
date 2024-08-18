package infodb.source.wp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import infodb.api.Article;

import java.time.Instant;
import java.util.List;

final class WpArticleMapper {
    private static final String origin = "Wirtualna Polska";

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

        return new Article(id, origin ,title, url, imageUrl, Instant.ofEpochSecond(createdAt), tags);
    }
}
