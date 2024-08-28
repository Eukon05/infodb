package infodb.app;

import infodb.api.Article;
import infodb.api.ArticleSource;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<ArticleSource> sources = ServiceLoader.load(ArticleSource.class);

        List<Article> combined = new ArrayList<>();
        for (ArticleSource source : sources) {
            combined.addAll(source.getLatest());
        }

        combined.forEach(System.out::println);
    }
}
