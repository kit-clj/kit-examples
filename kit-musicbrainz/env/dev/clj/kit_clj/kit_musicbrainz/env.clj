(ns kit-clj.kit-musicbrainz.env
  (:require
    [clojure.tools.logging :as log]
    [kit-clj.kit-musicbrainz.dev-middleware :refer [wrap-dev]]

    ;; test containers
    [kit-clj.kit-musicbrainz.cache.tc-redis]
    [kit-clj.kit-musicbrainz.db.tc-postgres]
))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[ starting using the development or test profile]=-"))
   :started    (fn []
                 (log/info "\n-=[ started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[ has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
