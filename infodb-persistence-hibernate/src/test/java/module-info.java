import ovh.eukon05.infodb.api.persistence.ArticleDAO;

module ovh.eukon05.infodb.persistence.hibernate.test {
    requires ovh.eukon05.infodb.api.persistence;
    requires ovh.eukon05.infodb.persistence.hibernate;
    requires org.junit.jupiter.api;
    opens ovh.eukon05.infodb.persistence.hibernate.test to org.junit.platform.commons;
    uses ArticleDAO;
}