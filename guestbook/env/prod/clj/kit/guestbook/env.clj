(ns kit.guestbook.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[ starting]=-"))
   :started    (fn []
                 (log/info "\n-=[ started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[ has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
