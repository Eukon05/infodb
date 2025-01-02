package ovh.eukon05.infodb.source.tvpinfo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ovh.eukon05.infodb.api.source.ArticleSourceAdapter;
import ovh.eukon05.infodb.api.source.ArticleSourceConnectionFailedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class TVPInfoAdapter extends ArticleSourceAdapter {
    private static final int TVPINFO_PAGE_LIMIT = 100;
    private static final Gson GSON = new Gson();
    private static final String LATEST_ARTICLES_URL = "https://www.tvp.info/api/info/list?id=71921924&page=%d&limit=%d";
    private static final String TAGS_URL = "https://www.tvp.info/api/info/meta?id=%s";

    private TVPInfoAdapter() {
    }

    static JsonArray getLatest(int limit) {
        if (limit <= 0)
            throw new IllegalArgumentException("Can't fetch a negative number of articles (provided limit is negative or zero)");

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request;
            HttpResponse<String> response;

            if (limit <= TVPINFO_PAGE_LIMIT) {
                request = prepareGetRequest(LATEST_ARTICLES_URL.formatted(1, limit));
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                checkResponseStatus(response.statusCode());

                return GSON.fromJson(response.body(), JsonObject.class).getAsJsonObject("data").getAsJsonArray("items");
            } else {
                int page = 1;
                JsonArray current;
                JsonArray result = new JsonArray();
                do {
                    request = prepareGetRequest(String.format(LATEST_ARTICLES_URL, page, TVPINFO_PAGE_LIMIT));
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    checkResponseStatus(response.statusCode());

                    current = GSON.fromJson(response.body(), JsonObject.class).getAsJsonObject("data").getAsJsonArray("items");

                    for (JsonElement element : current) {
                        if (limit == 0)
                            break;

                        result.add(element);
                        limit--;
                    }

                    page++;
                }
                while (limit > 0);

                return result;
            }
        } catch (IOException | InterruptedException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    static String[] getTags(String articleId) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TAGS_URL.formatted(articleId)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            checkResponseStatus(response.statusCode());

            return GSON.fromJson(response.body(), JsonObject.class)
                    .getAsJsonObject("data")
                    .get("meta_keywords")
                    .getAsString()
                    .split(", ");
        } catch (IOException | InterruptedException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }
}
