import ovh.eukon05.infodb.source.donald.DonaldSource;

module ovh.eukon05.infodb.source.donald {
    requires ovh.eukon05.infodb.api;
    requires com.google.gson;
    requires java.net.http;
    provides ovh.eukon05.infodb.api.ArticleSource with DonaldSource;
}