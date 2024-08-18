import infodb.source.donald.DonaldSource;

module infodb.source.donald {
    requires infodb.api;
    requires com.google.gson;
    requires java.net.http;
    provides infodb.api.ArticleSource with DonaldSource;
}