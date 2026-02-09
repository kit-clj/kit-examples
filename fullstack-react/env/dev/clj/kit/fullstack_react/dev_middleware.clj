(ns kit.fullstack-react.dev-middleware
  (:require
    [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-dev [handler _]
  (-> handler
      wrap-reload))
