# kit-musicbrainz

This is a demo application of a non-trivial backend API service built using the [kit framework](https://github.com/kit-clj/kit)

It shows off the power to build something complex in a short amount of time. The initial development for this (pending any future fixes, PRs, etc.) was done in an hour or so. Most of the time was spent wrangling with data importing. Base created with the kit template `clojure -X:new :template kit-clj :name kit-clj/kit-musicbrainz :args '[+quartz +redis +sql]'`

## Features

The demo shows off the following features

- Swagger UI
- Test Containers setup for services in development/testing with persistent data in dev
- Env specific resource file example
- Redis, PostgreSQL, and Quartz

## Local Setup

- JDK 11+
- Download the [MusicBrainz dataset](https://wiki.musicbrainz.org/MusicBrainz_Database/Download) into the `resources/seed` folder. This is the `mbdump.tar.bz2` file. Make sure you unarchive it into the seed folder. such that there is a folder /mbdump with many files

## Development

Start a REPL in your editor or terminal of choice.

Start the server with:

```clojure
(go)
```

If you haven't yet seeded your database, you can run

```clojure
(seed/seed-musicbrainz! (:db.sql/dev-postgres state/system))
```

The default API is found at localhost:3000/api

System configuration is found under `resources/system.edn`

Reload changes:

```clojure
(reset)
```

## Credits

Many thanks to the [MusicBrainz database](https://musicbrainz.org/)