import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.source.donald.DonaldSource;

module ovh.eukon05.infodb.source.donald {
    requires ovh.eukon05.infodb.api.source;
    requires com.google.gson;
    requires java.net.http;
    provides ArticleSource with DonaldSource;
}