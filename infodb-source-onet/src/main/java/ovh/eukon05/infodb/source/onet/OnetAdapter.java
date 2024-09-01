package ovh.eukon05.infodb.source.onet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class OnetAdapter {
    private static final String sourceUrl = "https://wiadomosci.onet.pl/?page=0&limit=%d&ajax=1";

    static Elements getLatest(int limit) {
        if (limit > 105)
            throw new IllegalArgumentException("ONET's API does not support fetching more than 105 latest articles");

        try {
            return Jsoup.connect(String.format(sourceUrl, limit)).get().getElementsByClass("itemBox");
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
