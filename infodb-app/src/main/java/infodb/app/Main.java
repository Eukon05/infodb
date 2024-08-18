package infodb.app;

import infodb.api.ArticleSource;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<ArticleSource> sources = ServiceLoader.load(ArticleSource.class);

        for (ArticleSource source : sources) {
            source.getLatest().forEach(System.out::println);
        }
    }
}
