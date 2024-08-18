package infodb.service.wp;

import com.google.gson.JsonObject;
import infodb.api.Article;

import java.time.LocalDateTime;

class WpArticleMapper {
    static Article mapFromJson(JsonObject articleJson) {
        String title = articleJson.get("title").getAsString();
        String id = articleJson.get("contentId").getAsString();
        String url = articleJson.get("url").getAsString();
        String imageUrl = articleJson.get("image").getAsString();
        return new Article(id, title, url, imageUrl, LocalDateTime.now());
    }
}
