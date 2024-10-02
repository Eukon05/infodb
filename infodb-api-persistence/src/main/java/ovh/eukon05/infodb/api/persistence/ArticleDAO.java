package ovh.eukon05.infodb.api.persistence;

import java.util.List;

public interface ArticleDAO {
    ArticleDTO findById(String id);

    boolean save(ArticleDTO article);

    List<ArticleDTO> getLatest(int pageNo);

    List<ArticleDTO> findByCriteria(ArticleSearchCriteria criteria);

    List<ArticleDTO> findByCriteria(ArticleSearchCriteria criteria, int pageNo);
}
