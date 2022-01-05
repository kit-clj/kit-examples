(ns kit-clj.kit-musicbrainz.seed-data
  (:require
    [clojure.java.shell :as shell]
    [clojure.string :as string]))

(defn seed-musicbrainz!
  [jdbc-url]
  (shell/sh "./env/dev/scripts/seed-db.sh" (string/replace jdbc-url
                                                           "jdbc:"
                                                           "")))

