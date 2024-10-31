package ovh.eukon05.infodb.app;

import org.springframework.web.bind.annotation.*;
import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.persistence.ArticleDTO;
import ovh.eukon05.infodb.api.persistence.ArticleSearchCriteria;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
class ApiController {
    private final ArticleDAO dao;

    public ApiController(List<ArticleDAO> daos) {
        dao = daos.get(0);
    }

    @GetMapping("/latest")
    public List<ArticleDTO> getLatest(@RequestParam(required = false, defaultValue = "0") int page) {
        return dao.getLatest(page);
    }

    @PostMapping
    public List<ArticleDTO> search(@RequestParam(required = false, defaultValue = "0") int page, @RequestBody ArticleSearchCriteria criteria) {
        return dao.findByCriteria(criteria, page);
    }

    @GetMapping("/{id}")
    public ArticleDTO getById(@PathVariable String id) {
        return dao.findById(id);
    }
}
