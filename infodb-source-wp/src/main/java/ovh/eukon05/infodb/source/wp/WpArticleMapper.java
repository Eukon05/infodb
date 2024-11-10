package ovh.eukon05.infodb.source.wp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.time.Instant;
import java.util.List;

final class WpArticleMapper {
    private static final String IMAGE_URL_TEMPLATE = "https://i.wpimg.pl/O/%dx%d/%s";
    private static final String IMAGE = "image";

    private WpArticleMapper() {
    }

    static Article mapFromJson(JsonObject articleJson, JsonObject articleDetailsJson, ArticleSourceInfo sourceInfo) {
        String title = articleJson.get("title").getAsString();
        String id = articleJson.get("contentId").getAsString();
        String url = articleJson.get("url").getAsString();
        String imageUrl = articleJson.get(IMAGE).getAsString().replace("https://", "");

        int imageHeight = articleDetailsJson.getAsJsonObject(IMAGE).get("height").getAsInt();
        int imageWidth = articleDetailsJson.getAsJsonObject(IMAGE).get("width").getAsInt();

        String finalImageUrl = String.format(IMAGE_URL_TEMPLATE, imageWidth, imageHeight, imageUrl);

        long createdAt = articleDetailsJson.get("created").getAsLong();
        List<String> tags = articleDetailsJson.getAsJsonArray("tags")
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(e -> e.get("slug"))
                .map(JsonElement::getAsString)
                .toList();

        return new Article(id, sourceInfo.name(), title, url, finalImageUrl, Instant.ofEpochSecond(createdAt), tags);
    }
}
