package ovh.eukon05.infodb.source.onet;

import org.jsoup.nodes.Element;
import ovh.eukon05.infodb.api.Article;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

final class OnetArticleMapper {
    private static final String imgPrefix = "https:%s";
    private static final String urlPrefix = "https://wiadomosci.onet.pl/%s";
    private static final String provider = "ONET";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");

    static Article mapFromHtml(Element articleElement, OnetArticleDetails details) {
        String urlLong = articleElement.attr("href");
        String[] tokens = urlLong.split("/");

        String id = tokens[tokens.length - 1];
        String url = String.format(urlPrefix, id);

        String imageUrl = String.format(imgPrefix, articleElement.getElementsByTag("img").attr("src"));
        String title = articleElement.getElementsByTag("span").text();

        // Article will have its publication time shifted to the UTC timezone this way!
        OffsetDateTime pubDate = OffsetDateTime.parse(details.pubDate(), dtf);

        return new Article(id, provider, title, url, imageUrl, pubDate.toInstant(), details.tags());
    }
}
