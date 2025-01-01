# infodb

## About the project

infodb aims to solve the problem of feeling out-of-touch with events happening in the world by combining multiple news
sources inside one app.  
The app polls the latest news articles from major Polish outlets, such as Onet or Wirtualna Polska, stores them in an
SQL database and provides a REST API allowing the user to easily search through its contents.

## Capabilities

Currently, the app supports four news sources:

- Onet
- Wirtualna Polska
- DonaldPL
- TVP Info

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

## How to run

The app can be run from source using the latest version of Maven 3 and Java 21 (or later).  
To run the app, clone the repo to a folder of your choice and run the following command in the root directory of the
project:

`
mvn clean package -DskipTests
`

You can run the tests if you want, but every commit is tested on push by GitHub Actions so this step can be skipped as
suggested.

After the build is finished, you can run the app with the following command:

```shell
cd infodb-app/target && \
java "-Dinfodb.sources.articlelimit=20" \
"-Dinfodb.hibernate.db.user=sa" \
"-Dinfodb.hibernate.db.pass= " \
"-Dinfodb.hibernate.db.url=jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1" \
--module-path "infodb-app-1.0-SNAPSHOT.jar:modules" \
-m ovh.eukon05.infodb.app/ovh.eukon05.infodb.app.Main
```

(on Windows, replace the colon with a semicolon in the module path and the backslashes with backticks `` ` ``).

Make sure to substitute the values of the system properties with your own.

By default, the app ships with an H2DB driver to allow for easy testing with an in-memory db, and an additional
PostgreSQL driver.  
To use a database of your own, download the necessary driver JAR and place it in the `infodb-app/target/modules`
directory and substitute the values in the launch command with the url and credentials of your db.

The default limit of articles fetched in one go (per source) is 20, but you can change it by modifying the launch
command. However, keep in mind that some sources have a maximum limit imposed by their API and will not allow the app to
fetch more than that. An example is Wirtualna Polska, with an API limit of 75 articles max.
