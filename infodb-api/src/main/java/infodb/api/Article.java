package infodb.api;

import java.time.LocalDateTime;

public record Article(String id, String title, String url, String imageUrl, LocalDateTime datePublished) {
}
