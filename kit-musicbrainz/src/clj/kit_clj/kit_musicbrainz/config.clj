(ns kit-clj.kit-musicbrainz.config
  (:require
    [clojure.java.io :as io]
    [kit.config :as config]))

(def ^:const system-filename "system.edn")
(def ^:const env-system-filename "env-system.edn")

(defn system-config
  [options]
  (merge
    (config/read-config system-filename options)

    ;; merge in optional environmental system config
    (let [env-system (io/file (io/resource env-system-filename))]
      (when (.exists env-system)
        (config/read-config env-system-filename options)))))
