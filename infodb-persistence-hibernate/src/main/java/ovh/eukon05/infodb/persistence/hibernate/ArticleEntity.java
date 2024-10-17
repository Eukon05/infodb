package ovh.eukon05.infodb.persistence.hibernate;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@Table(name = "Article")
class ArticleEntity {
    private static final Pattern urlRegex = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_+.~#?&/=]*)");

    @Id
    private String id;

    private String title;
    private String origin;
    private String url;
    private String imageUrl;
    private Instant datePublished;

    @ElementCollection
    private List<String> tags;

    public ArticleEntity() {
    }

    ArticleEntity(String id, String title, String url, String origin, String imageUrl, Instant datePublished, List<String> tags) {
        setId(id);
        setTitle(title);
        setUrl(url);
        setOrigin(origin);
        setImageUrl(imageUrl);
        setDatePublished(datePublished);
        setTags(tags);
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = Objects.requireNonNull(id);
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    String getOrigin() {
        return origin;
    }

    void setOrigin(String origin) {
        this.origin = Objects.requireNonNull(origin);
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        url = Objects.requireNonNull(url);

        if (urlRegex.matcher(url).matches())
            this.url = url;
        else
            throw new IllegalArgumentException("Url must be a valid URL");
    }

    String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        imageUrl = Objects.requireNonNull(imageUrl);

        if (urlRegex.matcher(imageUrl).matches())
            this.imageUrl = imageUrl;
        else {
            System.out.println(imageUrl);
            throw new IllegalArgumentException("ImageUrl must be a valid URL");
        }
    }

    Instant getDatePublished() {
        return datePublished;
    }

    void setDatePublished(Instant datePublished) {
        Objects.requireNonNull(datePublished);

        if (datePublished.isAfter(Instant.now()))
            throw new IllegalArgumentException("datePublished must be less than or equal to the current datetime");

        this.datePublished = datePublished;
    }

    List<String> getTags() {
        return tags;
    }

    void setTags(List<String> tags) {
        this.tags = Objects.requireNonNull(tags);
    }
}
