(ns kit-clj.kit-musicbrainz.db.tc-postgres
  (:require
    [clj-test-containers.core :as tc]
    [integrant.core :as ig]
    [kit.ig-utils :as ig-utils])
  (:import
    [org.testcontainers.containers GenericContainer]))

(def ^:const docker-image "postgres")
(def ^:const docker-version "14-alpine")
(def exposed-ports [5432])

(defmethod ig/init-key :testcontainers/postgres
  [_ {:keys [version data-path username password db]
      :or   {version docker-version}}]
  (cond-> (tc/init {:container     (GenericContainer. (str docker-image ":" version))
                    :exposed-ports exposed-ports
                    :env-vars      [["POSTGRES_DB" db]
                                    ["POSTGRES_USER" username]
                                    ["POSTGRES_PASSWORD" password]]
                    :wait-for      {:wait-strategy :log
                                    :message       "database system is ready to accept connections"}})

          (some? data-path)
          (tc/bind-filesystem! {:host-path      data-path
                                :container-path "/var/lib/postgresql/data/"
                                :mode           :read-write})

          true
          (tc/start!)

          true
          (assoc ::username username
                 ::password password
                 ::db db)))

(defmethod ig/halt-key! :testcontainers/postgres
  [_ instance]
  (tc/stop! instance))

(defmethod ig/suspend-key! :testcontainers/postgres [_ _])

(defmethod ig/resume-key :testcontainers/postgres
  [key opts old-opts old-impl]
  (ig-utils/resume-handler key opts old-opts old-impl))

(defmethod ig/init-key :db.sql/dev-postgres
  [_ {::keys [username password db] :keys [mapped-ports host]}]
  (str "jdbc:postgresql://" host ":" (get mapped-ports 5432) "/" db "?user=" username "&password=" password))