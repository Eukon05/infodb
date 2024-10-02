import ovh.eukon05.infodb.api.persistence.ArticleDAO;

module ovh.eukon05.infodb.persistence.hashmap {
    requires ovh.eukon05.infodb.api.persistence;
    provides ArticleDAO with ovh.eukon05.infodb.persistence.hashmap.HashmapDAO;
}