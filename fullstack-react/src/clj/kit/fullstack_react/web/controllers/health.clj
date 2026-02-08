(ns kit.fullstack-react.web.controllers.health
  (:require
    [clojure.core.cache :as cache]
    [clojure.tools.logging :as log]
    [kit.fullstack-react.web.routes.utils :as utils]
    [ring.util.http-response :as http-response])
  (:import
    [java.util Date]))

(defn- redis-status
  "Pings Redis and returns its status."
  [cache]
  (try
    (cache/miss cache "health-check" {:val "ok" :ttl 10})
    {:status "up"}
    (catch Exception e
      (log/warn "Redis health check failed:" (.getMessage e))
      {:status "down" :error (.getMessage e)})))

(defn- mysql-status
  "Queries MySQL and returns its status."
  [query-fn]
  (try
    (query-fn :get-messages {})
    {:status "up"}
    (catch Exception e
      (log/warn "MySQL health check failed:" (.getMessage e))
      {:status "down" :error (.getMessage e)})))

(defn healthcheck!
  [req]
  (let [{:keys [cache query-fn]} (utils/route-data req)]
    (http-response/ok
      {:time     (str (Date. (System/currentTimeMillis)))
       :up-since (str (Date. (.getStartTime (java.lang.management.ManagementFactory/getRuntimeMXBean))))
       :app      {:status "up"}
       :redis    (redis-status cache)
       :mysql    (mysql-status query-fn)})))
