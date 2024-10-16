import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.persistence.hibernate.HibernateDAO;

module ovh.eukon05.infodb.persistence.hibernate {
    requires ovh.eukon05.infodb.api.persistence;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    provides ArticleDAO with HibernateDAO;
    opens ovh.eukon05.infodb.persistence.hibernate to org.hibernate.orm.core;
}