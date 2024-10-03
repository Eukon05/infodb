package ovh.eukon05.infodb.source.onet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ovh.eukon05.infodb.api.source.ArticleSourceConnectionFailedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class OnetAdapter {
    private static final String SOURCE_URL = "https://wiadomosci.onet.pl/?page=%d&limit=%d&ajax=1";
    private static final int ONET_PAGE_LIMIT = 105;

    private OnetAdapter() {
    }

    static Elements getLatest(int limit) {
        if (limit <= 0)
            throw new IllegalArgumentException("Can't fetch a negative number of articles (provided limit is negative or zero)");

        try {
            Connection conn;
            if (limit <= ONET_PAGE_LIMIT) {
                conn = Jsoup.connect(String.format(SOURCE_URL, 0, limit));
                checkResponseStatus(conn.execute().statusCode());
                return conn.get().getElementsByClass("itemBox");
            }
            else {
                int page = 0;
                Elements elements = new Elements();

                do {
                    conn = Jsoup.connect(String.format(SOURCE_URL, page, ONET_PAGE_LIMIT));
                    checkResponseStatus(conn.execute().statusCode());

                    conn.get()
                            .getElementsByClass("itemBox")
                            .stream()
                            .limit(limit)
                            .forEach(elements::add);

                    page++;
                    limit -= ONET_PAGE_LIMIT;
                }
                while (limit > 0);

                return elements;
            }
        } catch (IOException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    static OnetArticleDetails getArticleDetails(String articleUrl) {
        try {
            Connection conn = Jsoup.connect(articleUrl);
            checkResponseStatus(conn.execute().statusCode());

            Document res = conn.get();
            String pubDate = res.getElementsByAttributeValue("property", "article:published_time").attr("content");

            List<String> tagsList = new ArrayList<>();

            res.getElementsByClass("relatedTopic").stream()
                    .map(e -> e.getElementsByTag("a").attr("href").replace("/", ""))
                    .forEach(tagsList::add);

            return new OnetArticleDetails(pubDate, tagsList);
        } catch (IOException e) {
            throw new ArticleSourceConnectionFailedException();
        }
    }

    private static void checkResponseStatus(int statusCode) {
        if (statusCode != 200) {
            throw new ArticleSourceConnectionFailedException(statusCode);
        }
    }
}
