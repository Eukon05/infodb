package ovh.eukon05.infodb.source.donald;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.Article;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

class DonaldArticleMapper {
    private static final String baseUrl = "https://www.donald.pl/artykuly/%s";
    private static final String origin = "DonaldPL";

    static Article mapFromJson(JsonObject articleDetailsJson) {
        String title = articleDetailsJson.get("title").getAsString();
        String id = articleDetailsJson.get("uuid").getAsString();
        String url = String.format(baseUrl, id);
        String imageUrl = articleDetailsJson.get("image").getAsString();
        Instant createdAt = ZonedDateTime.parse(articleDetailsJson.get("publication_date").getAsString()).toInstant();

        List<String> tags = articleDetailsJson.getAsJsonArray("tags")
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(e -> e.get("slug").getAsString())
                .toList();

        return new Article(id, origin, title, url, imageUrl, createdAt, tags);
    }
}
