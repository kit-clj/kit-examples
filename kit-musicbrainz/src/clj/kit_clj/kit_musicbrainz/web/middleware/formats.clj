(ns kit-clj.kit-musicbrainz.web.middleware.formats
  (:require
    [muuntaja.core :as m]))

(def instance
  (m/create m/default-options))
