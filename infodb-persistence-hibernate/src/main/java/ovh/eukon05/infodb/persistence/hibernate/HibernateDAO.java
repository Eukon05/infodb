package ovh.eukon05.infodb.persistence.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.persistence.ArticleSearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ArticleEntity> cr = cb.createQuery(ArticleEntity.class);
        Root<ArticleEntity> root = cr.from(ArticleEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (Optional.ofNullable(criteria.title()).isPresent()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + criteria.title().toLowerCase() + "%"));
        }
        if (Optional.ofNullable(criteria.origin()).isPresent()) {
            predicates.add(cb.equal(root.get("origin"), criteria.origin()));
        }
        if (Optional.ofNullable(criteria.dateFrom()).isPresent()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("datePublished"), criteria.dateFrom()));
        }
        if (Optional.ofNullable(criteria.dateTo()).isPresent()) {
            predicates.add(cb.lessThanOrEqualTo(root.get("datePublished"), criteria.dateTo()));
        }
        // This predicate checks if ALL the tags belong to the article, not if at least one!
        if (Optional.ofNullable(criteria.tags()).isPresent() && !criteria.tags().isEmpty()) {
            for (String tag : criteria.tags()) {
                predicates.add(cb.isMember(tag, root.get("tags")));
            }
        }

        cr.where(cb.and(predicates.toArray(new Predicate[]{}))).orderBy(cb.desc(root.get("datePublished")));
        return em.createQuery(cr)
                .setFirstResult(pageNo * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultStream()
                .map(ArticleEntityMapper::mapFromEntity)
                .toList();
    }
}
