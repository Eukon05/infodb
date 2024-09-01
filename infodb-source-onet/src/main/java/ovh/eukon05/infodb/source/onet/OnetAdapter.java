package ovh.eukon05.infodb.source.onet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class OnetAdapter {
    private static final String sourceUrl = "https://wiadomosci.onet.pl/?page=0&limit=75&ajax=1";

    static Elements getLatest() {
        try {
            return Jsoup.connect(sourceUrl).get().getElementsByClass("itemBox");
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
