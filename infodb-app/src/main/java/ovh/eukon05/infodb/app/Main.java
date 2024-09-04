package ovh.eukon05.infodb.app;

import ovh.eukon05.infodb.api.ArticleSource;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<ArticleSource> sources = ServiceLoader.load(ArticleSource.class);

        for (ArticleSource source : sources) {
            new Thread(() -> source.getLatest().forEach(System.out::println)).start();
        }
    }
}
