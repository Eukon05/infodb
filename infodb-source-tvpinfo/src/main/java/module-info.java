import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.source.tvpinfo.TVPInfoSource;

module ovh.eukon05.infodb.source.tvpinfo {
    requires ovh.eukon05.infodb.api.source;
    requires com.google.gson;
    requires java.net.http;
    provides ArticleSource with TVPInfoSource;
}