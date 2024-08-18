import infodb.source.wp.WpSource;

module infodb.source.wp {
    requires infodb.api;
    requires java.net.http;
    requires com.google.gson;
    provides infodb.api.ArticleSource with WpSource;
}