open module ovh.eukon05.infodb.api.test {
    requires transitive ovh.eukon05.infodb.api;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;
    exports ovh.eukon05.infodb.api.test;
    uses ovh.eukon05.infodb.api.ArticleSource;
}