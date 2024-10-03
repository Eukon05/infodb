package ovh.eukon05.infodb.source.onet;

import org.jsoup.nodes.Element;
import ovh.eukon05.infodb.api.source.Article;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

final class OnetArticleMapper {
    private static final String IMG_PREFIX = "https:%s";
    private static final String URL_PREFIX = "https://wiadomosci.onet.pl/%s";
    private static final String PROVIDER = "ONET";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");

    private OnetArticleMapper() {
    }

    static Article mapFromHtml(Element articleElement, OnetArticleDetails details) {
        String urlLong = articleElement.attr("href");
        String[] tokens = urlLong.split("/");

        String id = tokens[tokens.length - 1];
        String url = String.format(URL_PREFIX, id);

        String imageUrl = String.format(IMG_PREFIX, articleElement.getElementsByTag("img").attr("src"));
        String title = articleElement.getElementsByTag("span").text();

        // Article will have its publication time shifted to the UTC timezone this way!
        ZonedDateTime pubDate = ZonedDateTime.parse(details.pubDate(), DTF);

        return new Article(id, PROVIDER, title, url, imageUrl, pubDate.toInstant(), details.tags());
    }
}
