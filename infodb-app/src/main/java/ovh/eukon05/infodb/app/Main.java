package ovh.eukon05.infodb.app;

import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.source.ArticleSource;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<ArticleSource> sources = ServiceLoader.load(ArticleSource.class);
        ArticleDAO dao = null;

        for (ArticleDAO d : ServiceLoader.load(ArticleDAO.class)) {
            if (d.getClass().getName().contains("Hibernate")) {
                dao = d;
                break;
            }
        }

        for (ArticleSource source : sources) {
            source.getLatest(20).stream()
                    .map(e -> new ArticleDTO(e.id(), e.origin(), e.title(), e.url(), e.imageUrl(), e.datePublished(), e.tags()))
                    .forEach(dao::save);
        }

        //ArticleSearchCriteria criteria = new ArticleSearchCriteria(null, null, Instant.now().minus(2, ChronoUnit.HOURS), Instant.now().minus(30, ChronoUnit.MINUTES), null);
        //dao.findByCriteria(criteria, 0).forEach(System.out::println);
        dao.getLatest(0).forEach(System.out::println);
    }
}
