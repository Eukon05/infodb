package ovh.eukon05.infodb.api.persistence;

import java.time.Instant;
import java.util.List;

public record ArticleSearchCriteria(String title, String origin, Instant dateFrom, Instant dateTo, List<String> tags) {
}
