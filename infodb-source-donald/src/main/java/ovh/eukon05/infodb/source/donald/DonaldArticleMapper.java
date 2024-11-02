package ovh.eukon05.infodb.source.donald;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

class DonaldArticleMapper {
    private static final String BASE_URL = "https://www.donald.pl/artykuly/%s";

    private DonaldArticleMapper() {
    }

    static Article mapFromJson(JsonObject articleDetailsJson, ArticleSourceInfo sourceInfo) {
        String title = articleDetailsJson.get("title").getAsString();
        String id = articleDetailsJson.get("uuid").getAsString();
        String url = String.format(BASE_URL, id);
        String imageUrl = articleDetailsJson.get("image").getAsString();
        Instant createdAt = ZonedDateTime.parse(articleDetailsJson.get("publication_date").getAsString()).toInstant();

        List<String> tags = articleDetailsJson.getAsJsonArray("tags")
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(e -> e.get("slug").getAsString())
                .toList();

        return new Article(id, sourceInfo.name(), title, url, imageUrl, createdAt, tags);
    }
}
