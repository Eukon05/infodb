package ovh.eukon05.infodb.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ovh.eukon05.infodb.api.persistence.ArticleDAO;
import ovh.eukon05.infodb.api.source.ArticleSource;
import ovh.eukon05.infodb.api.source.ArticleSourceInfo;

import java.util.List;
import java.util.ServiceLoader;

@Configuration
class BeanConfig {

    @Bean
    List<ArticleDAO> articleDAOs() {
        ServiceLoader<ArticleDAO> loader = ServiceLoader.load(ArticleDAO.class);

        if (!loader.iterator().hasNext()) {
            throw new IllegalStateException("No ArticleDAO found");
        }

        return loader.stream().map(ServiceLoader.Provider::get).toList();
    }

    @Bean
    List<ArticleSource> articleSources() {
        ServiceLoader<ArticleSource> loader = ServiceLoader.load(ArticleSource.class);

        if (!loader.iterator().hasNext()) {
            throw new IllegalStateException("No ArticleSource found");
        }

        return loader.stream().map(ServiceLoader.Provider::get).toList();
    }

    @Bean
    List<ArticleSourceInfo> articleSourceInfos() {
        return articleSources().stream().map(ArticleSource::getSourceInfo).toList();
    }
}
