package ovh.eukon05.infodb.api.source;

public class ArticleSourceConnectionFailedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "The article source cannot be reached";
    private static final String STATUS_CODE_MESSAGE = "Article source returned an unexpected status code: %d";

    public ArticleSourceConnectionFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public ArticleSourceConnectionFailedException(int statusCode) {
        super(String.format(STATUS_CODE_MESSAGE, statusCode));
    }
}
