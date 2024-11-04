package ovh.eukon05.infodb.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.source.Article;
import ovh.eukon05.infodb.api.source.ArticleSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Service
class PersistenceScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceScheduler.class);
    private static final Function<Article, ArticleDTO> MAPPER = article -> new ArticleDTO(article.id(), article.origin(), article.title(), article.url(), article.imageUrl(), article.datePublished(), article.tags());

    private final List<ArticleSource> sources;
    private final List<ArticleDAO> daos;
    private final Set<String> cache = new HashSet<>();

    public PersistenceScheduler(List<ArticleSource> sources, List<ArticleDAO> daos) {
        this.sources = sources;
        this.daos = daos;
    }

    @Scheduled(initialDelay = 0, fixedDelayString = "${infodb.scheduler.delay}")
    private void fetchAndSave() {
        LOGGER.info("Scheduled article fetch started");

        sources.forEach(source -> {
            LOGGER.debug("Fetching articles from source: {}", source.getClass().getSimpleName());

            for (Article article : source.getLatest()) {
                if (cache.contains(article.id()))
                    continue;

                for (ArticleDAO dao : daos) {
                    if (dao.findById(article.id()) != null)
                        break;

                    LOGGER.debug("Saving article {} to {}", article, dao.getClass().getSimpleName());
                    dao.save(MAPPER.apply(article));
                }
                LOGGER.debug("Article {} successfully saved and cached", article.id());
                cache.add(article.id());
            }
        });

        LOGGER.info("Scheduled article fetch finished");
    }
}
