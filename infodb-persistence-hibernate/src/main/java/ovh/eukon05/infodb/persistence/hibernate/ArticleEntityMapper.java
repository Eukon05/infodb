package ovh.eukon05.infodb.persistence.hibernate;

import ovh.eukon05.infodb.api.persistence.ArticleDTO;

final class ArticleEntityMapper {
    static ArticleEntity mapFromDTO(ArticleDTO dto) {
        return new ArticleEntity(dto.id(), dto.title(), dto.url(), dto.origin(), dto.imageUrl(), dto.datePublished(), dto.tags());
    }

    static ArticleDTO mapFromEntity(ArticleEntity entity) {
        return new ArticleDTO(entity.getId(), entity.getOrigin(), entity.getTitle(), entity.getUrl(), entity.getImageUrl(), entity.getDatePublished(), entity.getTags());
    }
}
