package infodb.source.wp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

final class WpAdapter {
    private static final URI sourceURI = URI.create("https://wiadomosci.wp.pl/api/v1/data/graphql");
    private static final String latestArticlesQuery = """
            {
              "query": "query Recommendations { recommendations { newest(productIds: \\"5973184000386177\\", limit: 75, contentTypes: ARTICLE) { version count teasers { url type subtype slug title author sponsored publications { productId url } image contentId } } }}"
            }""";

    private static final String articleDetailsQuery = """
            {
              "query": "query Collections { collections(productId: \\"5973184000386177\\") { article(id: \\"%s\\") { created tags { slug } } }}"
            }""";

    private static final Gson gson = new Gson();

    static JsonArray getLatest(){
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest latestArticlesRequest = HttpRequest
                    .newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(latestArticlesQuery))
                    .uri(sourceURI)
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
                    .POST(HttpRequest.BodyPublishers.ofString(String.format(articleDetailsQuery, articleId)))
                    .uri(sourceURI)
                    .build();

            HttpResponse<String> response = client.send(articleDetailsRequest, HttpResponse.BodyHandlers.ofString());
            return extractArticleDetails(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonObject extractArticleDetails(String responseJson){
        return gson.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("collections")
                .getAsJsonObject("article");
    }

    private static JsonArray extractTeasers(String responseJson){
        return gson.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("recommendations")
                .getAsJsonObject("newest")
                .getAsJsonArray("teasers");
    }
}
