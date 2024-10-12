package ovh.eukon05.infodb.source.donald;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.ArticleSourceConnectionFailedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

class DonaldAdapter {
    private static final int DONALD_PAGE_LIMIT = 20;
    private static final Gson GSON = new Gson();
    private static final String LATEST_ARTICLES_URL;
    private static final String ARTICLE_DETAILS_URL = "https://www.donald.pl/api/v1/articles/%s";

    private DonaldAdapter() {
    }

    static {
        // DonaldPL's build ID changes from time to time, so we have to fetch the current one
        StringBuilder url = new StringBuilder("https://www.donald.pl/_next/data/");
        HttpRequest request = prepareGetRequest("https://www.donald.pl/news");

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            checkResponseStatus(response.statusCode());

            int start = response.body().indexOf("buildId") + 10;
            int end = start + 21;

            url.append(response.body(), start, end);
            url.append("/news.json");

            LATEST_ARTICLES_URL = url.toString();
        } catch (IOException | InterruptedException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    static List<String> getLatestIds(int limit) {
        if (limit <= 0)
            throw new IllegalArgumentException("Can't fetch a negative number of articles (provided limit is negative or zero)");

        try (HttpClient client = HttpClient.newHttpClient()) {
            List<String> result = new ArrayList<>();

            int page = 1;

            do {
                HttpRequest latestArticlesRequest = prepareGetRequest(String.format(LATEST_ARTICLES_URL, page));
                HttpResponse<String> response = client.send(latestArticlesRequest, HttpResponse.BodyHandlers.ofString());

                checkResponseStatus(response.statusCode());

                result.addAll(limit < DONALD_PAGE_LIMIT ? extractIds(response.body(), limit) : extractIds(response.body(), DONALD_PAGE_LIMIT));
                page++;
                limit -= DONALD_PAGE_LIMIT;
            }
            while (limit > 0);

            return result;
        } catch (IOException | InterruptedException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    static JsonObject getArticleDetails(String articleId){
        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {
            HttpRequest latestArticlesRequest = prepareGetRequest(String.format(ARTICLE_DETAILS_URL, articleId));
            HttpResponse<String> response = client.send(latestArticlesRequest, HttpResponse.BodyHandlers.ofString());

            checkResponseStatus(response.statusCode());

            return GSON.fromJson(response.body(), JsonObject.class);
        } catch (IOException | InterruptedException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    private static List<String> extractIds(String responseJson, int limit) {
        return GSON.fromJson(responseJson, JsonObject.class)
                .getAsJsonObject("pageProps")
                .getAsJsonArray("news")
                .asList()
                .stream()
                .limit(limit)
                .map(JsonElement::getAsJsonObject)
                .map(e -> e.get("uuid").getAsString())
                .toList();
    }

    private static HttpRequest prepareGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    private static void checkResponseStatus(int statusCode) {
        if (statusCode != 200) {
            throw new ArticleSourceConnectionFailedException(statusCode);
        }
    }
}
