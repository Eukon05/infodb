import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.source.ArticleSource;

open module ovh.eukon05.infodb.app {
    requires ovh.eukon05.infodb.api.source;
    requires ovh.eukon05.infodb.api.persistence;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires jakarta.persistence;
    requires org.slf4j;
    requires spring.web;
    requires io.swagger.v3.oas.annotations;
    requires spring.beans;
    requires org.apache.tomcat.embed.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires jakarta.xml.bind;
    requires jakarta.validation;
    uses ArticleSource;
    uses ArticleDAO;
}