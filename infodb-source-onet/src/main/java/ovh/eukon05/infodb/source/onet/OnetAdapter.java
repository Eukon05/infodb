package ovh.eukon05.infodb.source.onet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ovh.eukon05.infodb.api.source.ArticleSourceAdapter;
import ovh.eukon05.infodb.api.source.ArticleSourceConnectionFailedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class OnetAdapter extends ArticleSourceAdapter {
    private static final String SOURCE_URL = "https://wiadomosci.onet.pl/najnowsze";
    private static final int ONET_LIMIT = 99;
    private static final Pattern DATE_PUBLISHED = Pattern.compile("\"datePublished\"\\s*:\\s*\"([^\"]+)\"");

    private OnetAdapter() {
    }

    static List<OnetArticleSummary> getLatest(int limit) {
        if (limit <= 0)
            throw new IllegalArgumentException("Can't fetch a negative number of articles (provided limit is negative or zero)");
        else if (limit > ONET_LIMIT)
            throw new IllegalArgumentException("ONET does not support fetching more than 99 latest articles");

        try {
            Connection conn = Jsoup.connect(SOURCE_URL);
            checkResponseStatus(conn.execute().statusCode());

            List<Element> articleHeaders = conn.get().getElementsByClass("ods-o-card__link");

            return articleHeaders.stream().limit(limit).map(el -> {
                String url = el.attr("href");
                String title = el.getElementsByClass("ods-m-card-title").getFirst().text();
                String lead = el.getElementsByClass("ods-o-card__lead").getFirst().text();
                Element image = el.getElementsByClass("ods-a-photo").getFirst();
                String imageUrl = image.attr("src");

                return new OnetArticleSummary(title, lead, url, imageUrl);
            }).toList();

        } catch (IOException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    static OnetArticleDetails getArticleDetails(String articleUrl) {
        try {
            Connection conn = Jsoup.connect(articleUrl);
            checkResponseStatus(conn.execute().statusCode());

            Document res = conn.get();
            String pubDate = extractDateFromJsonLd(res);

            List<String> tagsList = new ArrayList<>();

            res.getElementsByClass("ods-m-tags__element")
                    .stream()
                    .map(e -> e.getElementsByTag("span").text())
                    .forEach(tagsList::add);

            return new OnetArticleDetails(pubDate, tagsList);
        } catch (IOException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    private static String extractDateFromJsonLd(Document page) {
        Element jsonLd = page.selectFirst("script[type=application/ld+json]");
        if (jsonLd == null)
            return "";

        String json = jsonLd.data();
        int newsArticleIdx = json.indexOf("\"NewsArticle\"");
        if (newsArticleIdx < 0)
            return "";

        Matcher matcher = DATE_PUBLISHED.matcher(json.substring(newsArticleIdx));
        return matcher.find() ? matcher.group(1) : "";
    }
}
