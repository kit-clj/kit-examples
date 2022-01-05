(ns kit-clj.kit-musicbrainz.web.controllers.artists
  (:require
    [clojure.core.cache :as cache]
    [clojure.tools.logging :as log]
    [kit-clj.kit-musicbrainz.web.routes.utils :as utils]
    [ring.util.http-response :as http-response]))

(derive :system.exception/business ::no-artist-id)
(derive :system.exception/business ::id-must-be-int)

(def ^:const default-ttl 3600)

(defn cache-lookup-or-add
  [cache key lookup-fn & [ttl]]
  (or (cache/lookup cache key)
      (let [value (lookup-fn)]
        (log/info "Cache missed for" key)
        (cache/miss cache key {:val value :ttl (or ttl default-ttl)})
        value)))

(defn get-by-id
  [{:keys [path-params] :as req}]
  (log/info "Artist lookup happening" path-params)
  (let [{:keys [cache query-fn]} (utils/route-data req)
        artist-id (try (Integer/parseInt (:id path-params))
                       (catch Exception _
                         (throw (ex-info "Error parsing ID"
                                         {:type ::id-must-be-int
                                          :id   (:id path-params)}))))]
    (if (some? artist-id)
      (let [res (cache-lookup-or-add cache (str "artist/" artist-id)
                                     ;; some quirk with next.jdbc and nippy
                                     ;; can fix by extending nippy but for this
                                     ;; demo we skipped that
                                     #(into {} (query-fn :artist-by-id {:id artist-id})))]
        (if (some? res)
          (http-response/ok res)
          (http-response/not-found)))
      (throw (ex-info "Invalid request (never should reach this thanks to reitit)" {:type ::no-artist-id})))))