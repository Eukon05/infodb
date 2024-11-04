# infodb

## About the project

infodb aims to solve the problem of feeling out-of-touch with events happening in the world by combining multiple news
sources inside one app.  
The app polls the latest news articles from major Polish outlets, such as Onet or Wirtualna Polska, stores them in an
SQL database and provides a REST API allowing the user to easily search through its contents.

## Capabilities

Currently, the app supports three news sources:

- Onet
- Wirtualna Polska
- DonaldPL

With more planned to be added later.

It relies on Hibernate for database connectivity, but can be easily expanded to support other database connectors.  
By default, PostgreSQL and H2DB drivers are included.

Articles are fetched from all available sources and saved to all available databases in fixed intervals, which can be
configured by the user. The default delay between fetches is 5 minutes.  
The user can also configure how many articles they would like the app to fetch in one go, the default being 20 per
source.  
The article ids are cached in a queue to avoid putting too much stress on the databases when checking if the app fetched
an article that is already saved.

The app exposes a REST API on `[hostname]:[port]/api/v1` with the following endpoints:

- articles
    - A POST endpoint that allows for searching through the database. It requires a request body of the following
      format (though no field is necessary and all of them can be null):
      ```json
      {
        "title": "string to be found in the article's title",
        "origin": "source of the article, has to be an exact name which can be found on the /api/v1/sources endpoint",
        "dateFrom": "an Instant representing the furthest date on which the article could be published",
        "dateTo": "an Instant representing the nearest date on which the article could be published",
        "tags": ["a", "collection", "of", "tags", "the", "article", "should", "be", "tagged", "with", "(although only one matching tag is enough for a match)"]
      }
      ```
      The endpoint supports pagination with an optional `page` request parameter, with indexing starting from 0.
- articles/latest
    - A GET endpoint which returns 20 of latest articles.  
      The endpoint supports pagination with an optional `page` request parameter, with indexing starting from 0.
- articles/{id}
    - A GET endpoint, returning an article matching the provided ID.
- sources
    - A GET endpoint, returning information on all the sources infodb uses. Names of the sources required for searching
      through the article database can be found here.

Documentation of the API can be accessed at `[hostname]:[port]/api/v1/api-docs`, with SwaggerUI available on
`[hostname]:[port]/api/v1/swagger-ui.html`.

## Architecture

Built using the Java Platform Module System, the app supports loading multiple news sources and database implementations
without having a tight dependency on any of them.  
You can add your own news source or database connector by implementing the provided interfaces and adding your module to
the module path on the app's launch - it should be automatically detected by the ServiceLoader!

This architecture allows for easy expansion of the projects capabilities without modifying the existing code; an
approach I had on the back of my mind throughout the entire process of making the app.  
I was heavily inspired by "hexagonal architecture", or "ports-and-adapters", which makes sure that every component of
the app is independent of the other.

Every article source module and every database module contains unit tests to check if it behaves as expected.