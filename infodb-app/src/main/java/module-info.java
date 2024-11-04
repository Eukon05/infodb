import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.source.ArticleSource;

open module ovh.eukon05.infodb.app {
    requires ovh.eukon05.infodb.api.source;
    requires ovh.eukon05.infodb.api.persistence;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires jakarta.persistence;
    requires org.slf4j;
    requires spring.web;
    requires io.swagger.v3.oas.annotations;
    uses ArticleSource;
    uses ArticleDAO;
}