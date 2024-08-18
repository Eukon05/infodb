package infodb.source.donald;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

class DonaldAdapter {
    private static final Gson gson = new Gson();
    private static final URI latestArticlesURI = URI.create("https://www.donald.pl/_next/data/58oTIJ0Co8UxCd-940jRU/news.json?page=1");
    private static final String articleDetailsUrl = "https://www.donald.pl/api/v1/articles/%s";

    static List<String> getLatestIds(){
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest latestArticlesRequest = HttpRequest
                    .newBuilder()
                    .uri(latestArticlesURI)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(latestArticlesRequest, HttpResponse.BodyHandlers.ofString());
            return extractIds(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static JsonObject getArticleDetails(String articleId){
        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {

            HttpRequest latestArticlesRequest = HttpRequest
                    .newBuilder()
                    .uri(URI.create(String.format(articleDetailsUrl, articleId)))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(latestArticlesRequest, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), JsonObject.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> extractIds(String responseJson){
        return gson.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("pageProps")
                .getAsJsonArray("news")
                .asList().stream().map(JsonElement::getAsJsonObject).map(e -> e.get("uuid").getAsString()).toList();
    }
}
