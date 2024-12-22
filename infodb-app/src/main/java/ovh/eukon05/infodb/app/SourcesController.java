package ovh.eukon05.infodb.app;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sources")
@CrossOrigin
@Tag(name = "Sources", description = "API methods exposing details about article sources used by infodb")
class SourcesController {
    private final List<ArticleSourceInfo> sourceInfoList;

    public SourcesController(List<ArticleSourceInfo> sourceInfoList) {
        this.sourceInfoList = sourceInfoList;
    }

    @GetMapping
    public List<ArticleSourceInfo> getSources() {
        return sourceInfoList;
    }
}
