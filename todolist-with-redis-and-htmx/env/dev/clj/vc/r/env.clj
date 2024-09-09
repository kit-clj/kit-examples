(ns vc.r.env
  (:require
    [clojure.tools.logging :as log]
    [vc.r.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[r starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[r started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[r has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
