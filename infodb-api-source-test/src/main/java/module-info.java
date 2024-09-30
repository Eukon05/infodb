open module ovh.eukon05.infodb.api.source.test {
    requires transitive ovh.eukon05.infodb.api.source;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;
    exports ovh.eukon05.infodb.api.source.test;
    uses ArticleSource;
}