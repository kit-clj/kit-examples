# fullstack-react

A full-stack example application built with the [Kit framework](https://github.com/kit-clj/kit), demonstrating how to integrate a Clojure backend with a React frontend, MySQL for persistence, and Redis for caching.

Inspired by [shogidude/clojure-kit-with-redis-mysql-react](https://github.com/shogidude/clojure-kit-with-redis-mysql-react) (see [kit-clj/kit#142](https://github.com/kit-clj/kit/issues/142)).

## Features

- **MySQL** via `kit-sql` with HugSQL queries and Migratus migrations
- **Redis** via `kit-redis` (Carmine), injected through Integrant
- **React** frontend (Vite) with API proxy to the Kit backend
- **Swagger UI** at `/api` for exploring the API
- **docker-compose** for MySQL and Redis services
- **Health endpoint** reporting status of all services
- Proper Integrant dependency injection (no `integrant.repl.state` in src)

## Prerequisites

- JDK 11+
- [Clojure CLI](https://clojure.org/guides/install_clojure)
- Node.js 18+
- Docker and Docker Compose (for MySQL and Redis)

## Getting Started

Start MySQL and Redis:

```sh
docker compose up -d
```

Install frontend dependencies:

```sh
cd client && npm install
```

Start a REPL in your editor or terminal of choice.

Start the server with:

```clojure
(go)
```

The API is available at http://localhost:3000/api

Start the React dev server (in a separate terminal):

```sh
cd client && npm run dev
```

The frontend is available at http://localhost:3001 and proxies API calls to the Kit backend.

System configuration is found under `resources/system.edn`.

Reload changes:

```clojure
(reset)
```

## Deploying the React Frontend

Build the React app and copy it into Kit's static resources:

```sh
make client-deploy
```

After this, the React app is served directly by Kit at http://localhost:3000.

## Building for Production

```sh
make uberjar
```

Run with environment variables:

```sh
JDBC_URL="jdbc:mysql://host:3306/mydb?user=prod&password=secret" \
REDIS_URI="redis://host:6379" \
java -jar target/fullstack-react-standalone.jar
```

## Project Structure

```
fullstack-react/
  client/              React app (Vite)
  src/clj/             Clojure source
    kit/fullstack_react/
      core.clj           Entry point
      config.clj         Configuration loading
      web/
        handler.clj      Ring handler + Reitit router
        controllers/
          health.clj     Health check (MySQL + Redis status)
          messages.clj   CRUD for messages
        routes/
          api.clj        API route definitions
        middleware/
          core.clj       Base middleware stack
          exception.clj  Exception handling
          formats.clj    Content negotiation (Muuntaja)
  resources/
    system.edn         Integrant system configuration
    sql/queries.sql    HugSQL query definitions
    migrations/        Migratus migration files
  env/                 Dev/prod/test environment configs
  docker-compose.yml   MySQL + Redis services
```
