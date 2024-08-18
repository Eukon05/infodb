package infodb.service.wp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import infodb.api.Article;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public final class WpSource implements infodb.api.ArticleSource {
    private final List<String> cachedIDs = new ArrayList<>();
    private static final URI sourceURI = URI.create("https://wiadomosci.wp.pl/api/v1/data/graphql");
    private static final String latestArticlesQuery = """
            {
              "query": "query Recommendations { recommendations { newest(productIds: \\"5973184000386177\\", limit: %d) { version count teasers { url type subtype slug title author sponsored publications { productId url } image contentId } } }}"
            }""";

    private static final Gson gson = new Gson();

    @Override
    public List<Article> getLatest(int num) {
        if(num > 75)
            throw new IllegalArgumentException("WP's API does not support fetching more than 75 latest articles at once");


        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest latestArticlesRequest = HttpRequest
                    .newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(String.format(latestArticlesQuery, num)))
                    .uri(sourceURI)
                    .build();

            HttpResponse<String> response = client.send(latestArticlesRequest, HttpResponse.BodyHandlers.ofString());
            JsonArray json = extractTeasers(response.body());

            return json.asList().stream().map(JsonElement::getAsJsonObject).map(WpArticleMapper::mapFromJson).toList();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonArray extractTeasers(String responseJson){
        return gson.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("recommendations")
                .getAsJsonObject("newest")
                .getAsJsonArray("teasers");
    }
}
