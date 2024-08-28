import infodb.source.onet.OnetSource;

module infodb.source.onet {
    requires infodb.api;
    requires org.jsoup;
    provides infodb.api.ArticleSource with OnetSource;
}