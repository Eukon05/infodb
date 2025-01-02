package ovh.eukon05.infodb.source.tvpinfo;

import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.time.Instant;
import java.util.Arrays;

class TVPInfoArticleMapper {
    private TVPInfoArticleMapper() {
    }

    static Article mapFromJson(JsonObject json, ArticleSourceInfo sourceInfo, String[] tags) {
        String id = json.get("_id").getAsString();
        String title = json.get("title").getAsString();
        String url = json.get("url").getAsString();

        JsonObject image = json.getAsJsonObject("image");
        String imageWidth = image.get("width").getAsString();
        String imageHeight = image.get("height").getAsString();

        String imageUrl = image.get("url").getAsString()
                .replace("{width}", imageWidth)
                .replace("{height}", imageHeight);

        Instant publishedAt = Instant.ofEpochMilli(json.get("release_date").getAsLong());

        return new Article(id, sourceInfo.name(), title, url, imageUrl, publishedAt, Arrays.asList(tags));
    }
}
