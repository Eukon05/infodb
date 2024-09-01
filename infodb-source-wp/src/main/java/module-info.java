import ovh.eukon05.infodb.api.ArticleSource;
import ovh.eukon05.infodb.source.wp.WpSource;

module ovh.eukon05.infodb.source.wp {
    requires ovh.eukon05.infodb.api;
    requires java.net.http;
    requires com.google.gson;
    provides ArticleSource with WpSource;
}