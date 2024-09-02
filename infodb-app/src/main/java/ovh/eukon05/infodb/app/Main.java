package ovh.eukon05.infodb.app;

import ovh.eukon05.infodb.api.ArticleSource;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ServiceLoader<ArticleSource> sources = ServiceLoader.load(ArticleSource.class);

        // Test for Onet source's extended article limit

        for (ArticleSource source : sources) {
            if (source.getClass().getName().toLowerCase().contains("onet")) {
                try (PrintWriter writer = new PrintWriter("out.txt")) {
                    source.getLatest(210).forEach(a -> writer.println(a.id()));
                }
            }
        }

    }
}
