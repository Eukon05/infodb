import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.source.wp.WpSource;

module ovh.eukon05.infodb.source.wp {
    requires ovh.eukon05.infodb.api.source;
    requires java.net.http;
    requires com.google.gson;
    provides ArticleSource with WpSource;
}