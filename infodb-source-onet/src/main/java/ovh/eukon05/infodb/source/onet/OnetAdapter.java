package ovh.eukon05.infodb.source.onet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
            if (limit <= ONET_PAGE_LIMIT)
                return Jsoup.connect(String.format(SOURCE_URL, 0, limit)).get().getElementsByClass("itemBox");
            else {
                int page = 0;

                Elements elements = new Elements();

                do {
                    Jsoup.connect(String.format(SOURCE_URL, page, ONET_PAGE_LIMIT))
                            .get()
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
            throw new RuntimeException(e);
        }
    }

    static OnetArticleDetails getArticleDetails(String articleUrl) {
        try {
            Document res = Jsoup.connect(articleUrl).get();
            String pubDate = res.getElementsByAttributeValue("property", "article:published_time").attr("content");

            List<String> tagsList = new ArrayList<>();

            res.getElementsByClass("relatedTopic").stream()
                    .map(e -> e.getElementsByTag("a").attr("href").replace("/", ""))
                    .forEach(tagsList::add);

            return new OnetArticleDetails(pubDate, tagsList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
