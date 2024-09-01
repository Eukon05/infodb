import ovh.eukon05.infodb.api.ArticleSource;
import ovh.eukon05.infodb.source.onet.OnetSource;

module ovh.eukon05.infodb.source.onet {
    requires ovh.eukon05.infodb.api;
    requires org.jsoup;
    provides ArticleSource with OnetSource;
}