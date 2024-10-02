import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.source.ArticleSource;

module ovh.eukon05.infodb.app {
    requires ovh.eukon05.infodb.api.source;
    requires ovh.eukon05.infodb.api.persistence;
    uses ArticleSource;
    uses ArticleDAO;
}