package ovh.eukon05.infodb.persistence.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.persistence.ArticleSearchCriteria;

import java.util.List;

public class HibernateDAO implements ArticleDAO {
    private static final int PAGE_SIZE = 20;
    private final EntityManager em = Persistence.createEntityManagerFactory("hibernate").createEntityManager();

    @Override
    public ArticleDTO findById(String id) {
        return em.find(ArticleDTO.class, id);
    }

    @Override
    public boolean save(ArticleDTO article) {
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            em.persist(ArticleEntityMapper.mapFromDTO(article));
            t.commit();
            return true;
        } catch (Exception e) {
            if (t.isActive()) t.rollback();
            throw e;
        }
    }

    @Override
    public List<ArticleDTO> getLatest(int pageNo) {
        TypedQuery<ArticleEntity> q = em.createQuery("select a from ArticleEntity a order by a.datePublished desc", ArticleEntity.class);
        q.setFirstResult(pageNo * PAGE_SIZE);
        q.setMaxResults(PAGE_SIZE);
        return q.getResultStream().map(ArticleEntityMapper::mapFromEntity).toList();
    }

    @Override
    public List<ArticleDTO> findByCriteria(ArticleSearchCriteria criteria) {
        return findByCriteria(criteria, 0);
    }

    @Override
    public List<ArticleDTO> findByCriteria(ArticleSearchCriteria criteria, int pageNo) {
        return List.of();
    }
}
