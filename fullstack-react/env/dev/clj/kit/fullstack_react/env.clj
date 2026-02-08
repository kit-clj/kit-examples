(ns kit.fullstack-react.env
  (:require
    [clojure.tools.logging :as log]
    [kit.fullstack-react.dev-middleware :as dev-middleware]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[ starting using the development or test profile]=-"))
   :started    (fn []
                 (log/info "\n-=[ started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[ has shut down successfully]=-"))
   :middleware dev-middleware/wrap-dev
   :opts       {:profile :dev}})
