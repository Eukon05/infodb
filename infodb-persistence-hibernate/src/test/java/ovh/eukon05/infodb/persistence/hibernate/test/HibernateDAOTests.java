package ovh.eukon05.infodb.persistence.hibernate.test;

import org.junit.jupiter.api.Test;
import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.persistence.ArticleSearchCriteria;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HibernateDAOTests {
    private final ArticleDAO dao = ServiceLoader.load(ArticleDAO.class).iterator().next();
    private static final String TEST_ORIGIN = "TEST";
    private static final String TEST_URL = "https://test.com/%s";
    private static final String TEST_IMG_URL = "https://test.com/%s/image.png";

    @Test
    void should_save_and_find_article() {
        String id = "nexttestid";

        ArticleDTO dto = new ArticleDTO(id, TEST_ORIGIN, "Test Article 2", TEST_URL.formatted(id), TEST_IMG_URL.formatted(id), Instant.now().minus(30, ChronoUnit.DAYS), List.of("test-tag"));
        dao.save(dto);
        assertEquals(dto, dao.findById(id));
    }

    @Test
    void should_search_for_article() {
        String id = "findme";

        ArticleDTO dto = new ArticleDTO(id, TEST_ORIGIN, "Find Me", TEST_URL.formatted(id), TEST_IMG_URL.formatted(id), Instant.now().minus(10, ChronoUnit.DAYS), List.of("find-me-tag"));
        dao.save(dto);

        ArticleSearchCriteria criteria = new ArticleSearchCriteria("Find Me", TEST_ORIGIN, Instant.now().minus(30, ChronoUnit.DAYS), Instant.now().minus(5, ChronoUnit.DAYS), List.of("find-me-tag"));

        assertTrue(dao.findByCriteria(criteria).contains(dto));
    }

    @Test
    void should_get_latest() {
        List<ArticleDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            dtos.add(new ArticleDTO("art%d".formatted(i), TEST_ORIGIN, "Test Article %d".formatted(i), TEST_URL.formatted(i), TEST_IMG_URL.formatted(i), Instant.now().minus(i, ChronoUnit.HOURS), Collections.emptyList()));
        }

        Collections.shuffle(dtos);
        dtos.forEach(dao::save);

        dtos.sort(Comparator.comparing(ArticleDTO::datePublished).reversed());
        assertTrue(dao.getLatest(0).containsAll(dtos.subList(0, 20)));
    }

    @Test
    void should_not_save_invalid_article() {
        //This one should fail because of the null strings
        ArticleDTO inv = new ArticleDTO(null, null, null, "thisisnotaurl", "totallynot", Instant.now().plus(30, ChronoUnit.DAYS), Collections.emptyList());
        assertThrows(NullPointerException.class, () -> dao.save(inv));

        //This one should fail because of the URLs
        ArticleDTO inv2 = new ArticleDTO("invalidarticle", "TEST", "Invalid", "thisisnotaurl", "totallynot", Instant.now().plus(30, ChronoUnit.DAYS), Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> dao.save(inv2));

        //This one should fail because of the date
        ArticleDTO inv3 = new ArticleDTO("invalidarticle", "TEST", "Invalid", TEST_URL.formatted("t"), TEST_IMG_URL.formatted("t"), Instant.now().plus(30, ChronoUnit.DAYS), Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> dao.save(inv3));
    }
}
