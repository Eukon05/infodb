package ovh.eukon05.infodb.api.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record ArticleSearchCriteria(String title, String origin, Instant dateFrom, Instant dateTo, List<String> tags) {
    public ArticleSearchCriteria {
        if (Optional.ofNullable(dateFrom).isPresent() && dateFrom.isAfter(Instant.now()))
            throw new IllegalArgumentException("dateFrom must be less than or equal to the current datetime");

        if (Optional.ofNullable(dateTo).isPresent() && dateTo.isAfter(Instant.now()))
            throw new IllegalArgumentException("dateTo must be less than or equal to the current datetime");

        if (Optional.ofNullable(dateFrom).isPresent() && Optional.ofNullable(dateTo).isPresent() && dateFrom.isAfter(dateTo))
            throw new IllegalArgumentException("dateFrom must be less than or equal to dateTo");
    }
}