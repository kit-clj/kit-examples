(ns vc.r.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[r starting]=-"))
   :start      (fn []
                 (log/info "\n-=[r started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[r has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
