(ns kit-clj.kit-musicbrainz.cache.tc-redis
  (:require
    [clj-test-containers.core :as tc]
    [integrant.core :as ig]
    [kit.ig-utils :as ig-utils])
  (:import
    [org.testcontainers.containers GenericContainer]))

(def ^:const docker-image "redis")
(def ^:const docker-version "6-alpine")
(def exposed-ports [6379])

(defmethod ig/init-key :testcontainers/redis
  [_ {:keys [version data-path]
      :or   {version docker-version}}]
  (cond-> (tc/init {:container     (GenericContainer. (str docker-image ":" version))
                    :exposed-ports exposed-ports})

          (some? data-path)
          (tc/bind-filesystem! {:host-path      data-path
                                :container-path "/data"
                                :mode           :read-write})

          true
          (tc/start!)))

(defmethod ig/halt-key! :testcontainers/redis
  [_ instance]
  (tc/stop! instance))

(defmethod ig/suspend-key! :testcontainers/redis [_ _])

(defmethod ig/resume-key :testcontainers/redis
  [key opts old-opts old-impl]
  (ig-utils/resume-handler key opts old-opts old-impl))

(defmethod ig/init-key :cache/dev-redis
  [_ {:keys [mapped-ports host]}]
  (str "http://" host ":" (get mapped-ports 6379)))