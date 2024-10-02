package ovh.eukon05.infodb.source.wp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

final class WpAdapter {
    private static final URI SOURCE_URI = URI.create("https://wiadomosci.wp.pl/api/v1/data/graphql");
    private static final String LATEST_ARTICLES_QUERY = """
            {
              "query": "query Recommendations { recommendations { newest(productIds: \\"5973184000386177\\", limit: %d, contentTypes: ARTICLE) { version count teasers { url type subtype slug title author sponsored publications { productId url } image contentId } } }}"
            }""";

    private static final String ARTICLE_DETAILS_QUERY = """
            {
              "query": "query Collections { collections(productId: \\"5973184000386177\\") { article(id: \\"%s\\") { created tags { slug } } }}"
            }""";

    private static final Gson GSON = new Gson();

    private WpAdapter() {
    }

    static JsonArray getLatest(int limit) {
        if (limit <= 0)
            throw new IllegalArgumentException("Can't fetch a negative number of articles (provided limit is negative or zero)");
        else if (limit > 75)
            throw new IllegalArgumentException("WP's API does not support fetching more than 75 latest articles");

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest latestArticlesRequest = HttpRequest
                    .newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(String.format(LATEST_ARTICLES_QUERY, limit)))
                    .uri(SOURCE_URI)
                    .build();

            HttpResponse<String> response = client.send(latestArticlesRequest, HttpResponse.BodyHandlers.ofString());
            return extractTeasers(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static JsonObject getArticleDetails(String articleId){
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest articleDetailsRequest = HttpRequest
                    .newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(String.format(ARTICLE_DETAILS_QUERY, articleId)))
                    .uri(SOURCE_URI)
                    .build();

            HttpResponse<String> response = client.send(articleDetailsRequest, HttpResponse.BodyHandlers.ofString());
            return extractArticleDetails(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonObject extractArticleDetails(String responseJson){
        return GSON.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("collections")
                .getAsJsonObject("article");
    }

    private static JsonArray extractTeasers(String responseJson){
        return GSON.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("recommendations")
                .getAsJsonObject("newest")
                .getAsJsonArray("teasers");
    }
}
