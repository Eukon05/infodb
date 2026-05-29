package ovh.eukon05.infodb.source.onet;

import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

final class OnetArticleMapper {
    private static final DateTimeFormatter RFC_822_OFFSET_DATE_TIME = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssZ");

    private OnetArticleMapper() {
    }

    static Article mapToArticle(OnetArticleSummary summary, OnetArticleDetails details, ArticleSourceInfo sourceInfo) {
        String[] tokens = summary.url().split("/");

        String id = tokens[tokens.length - 1];
        // Article will have its publication time shifted to the UTC timezone this way
        OffsetDateTime pubDate = OffsetDateTime.parse(details.pubDate(), RFC_822_OFFSET_DATE_TIME);

        return new Article(id, sourceInfo.name(), summary.title(), summary.url(), summary.imageUrl(), pubDate.toInstant(), details.tags());
    }
}
