(ns kit-clj.kit-musicbrainz.web.controllers.health
  (:require
    [kit-clj.kit-musicbrainz.messages.motd :as motd]
    [kit-clj.kit-musicbrainz.web.routes.utils :as utils]
    [ring.util.http-response :as http-response])
  (:import
    [java.util Date]))

(defn healthcheck!
  [req]
  (http-response/ok
    {:time     (str (Date. (System/currentTimeMillis)))
     :up-since (str (Date. (.getStartTime (java.lang.management.ManagementFactory/getRuntimeMXBean))))
     :app      {:status  "up"
                :message (motd/latest-message! {:cache (utils/route-data-key req :cache)})}}))
