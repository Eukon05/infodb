package ovh.eukon05.infodb.source.tvpinfo;

import com.google.gson.JsonElement;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.util.List;

public class TVPInfoSource implements ArticleSource {
    private static final ArticleSourceInfo SOURCE_INFO = new ArticleSourceInfo("TVPINFO", "https://www.tvp.info/informacje");

    @Override
    public List<Article> getLatest(int limit) {
        return TVPInfoAdapter.getLatest(limit)
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(json -> TVPInfoArticleMapper.mapFromJson(json, SOURCE_INFO))
                .toList();
    }

    @Override
    public ArticleSourceInfo getSourceInfo() {
        return SOURCE_INFO;
    }
}
