package ovh.eukon05.infodb.api;

import java.time.Instant;
import java.util.List;

public record Article(String id, String origin, String title, String url, String imageUrl, Instant datePublished, List<String> tags) {
}
