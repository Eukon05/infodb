package ovh.eukon05.infodb.persistence.hashmap;

import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.persistence.ArticleSearchCriteria;

import java.util.*;

public class HashmapDAO implements ArticleDAO {
    // I'm reusing the DTO here because it's just a simulation of a real db, and we don't need to create a db entity object
    private final HashMap<String, ArticleDTO> repository = new HashMap<>();
    private static final long PAGE_SIZE = 20;

    @Override
    public ArticleDTO findById(String id) {
        return repository.get(id);
    }

    @Override
    public boolean save(ArticleDTO article) {
        repository.put(article.id(), article);
        return true;
    }

    @Override
    public List<ArticleDTO> getLatest(int pageNo) {
        return repository.values().stream().sorted(Comparator.comparing(ArticleDTO::datePublished)).skip(PAGE_SIZE * pageNo).limit(PAGE_SIZE).toList();
    }

    @Override
    public List<ArticleDTO> findByCriteria(ArticleSearchCriteria criteria) {
        return findByCriteria(criteria, 0);
    }

    @Override
    public List<ArticleDTO> findByCriteria(ArticleSearchCriteria criteria, int pageNo) {
        return repository.values().stream().filter(e -> {
            if (Optional.ofNullable(criteria.title()).isPresent() && !e.title().toLowerCase().contains(criteria.title().toLowerCase())) {
                return false;
            }
            if (Optional.ofNullable(criteria.origin()).isPresent() && !e.origin().equals(criteria.origin())) {
                return false;
            }
            if (Optional.ofNullable(criteria.tags()).isPresent() && Collections.disjoint(e.tags(), criteria.tags())) {
                return false;
            }
            if (Optional.ofNullable(criteria.dateFrom()).isPresent() && e.datePublished().compareTo(criteria.dateFrom()) < 0) {
                return false;
            }
            return Optional.ofNullable(criteria.dateTo()).isEmpty() || e.datePublished().compareTo(criteria.dateTo()) <= 0;
        }).sorted(Comparator.comparing(ArticleDTO::datePublished).reversed()).skip(PAGE_SIZE * pageNo).limit(PAGE_SIZE).toList();
    }
}
