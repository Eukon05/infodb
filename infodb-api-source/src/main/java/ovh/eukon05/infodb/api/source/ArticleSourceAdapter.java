package ovh.eukon05.infodb.api.source;

import java.net.URI;
import java.net.http.HttpRequest;

public abstract class ArticleSourceAdapter {
    protected static HttpRequest prepareGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    protected static HttpRequest preparePostRequest(String url, String body) {
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .build();
    }

    protected static void checkResponseStatus(int statusCode) {
        if (statusCode != 200) {
            throw new ArticleSourceConnectionFailedException(statusCode);
        }
    }
}
