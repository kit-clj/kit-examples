(ns kit.fullstack-react.core
  (:require
    [clojure.tools.logging :as log]
    [integrant.core :as ig]
    [kit.fullstack-react.config :as config]
    [kit.fullstack-react.env :refer [defaults]]

    ;; Edges
    [kit.edge.cache.redis]
    [kit.edge.db.sql]
    [kit.edge.utils.repl]
    [kit.edge.server.undertow]
    [kit.fullstack-react.web.handler]

    ;; Routes
    [kit.fullstack-react.web.routes.api])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
  (reify Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread ex]
      (log/error {:what :uncaught-exception
                  :exception ex
                  :where (str "Uncaught exception on" (.getName thread))}))))

(defonce system (atom nil))

(defn stop-app []
  ((or (:stop defaults) (fn [])))
  (some-> (deref system) (ig/halt!))
  (shutdown-agents))

(defn start-app [& [params]]
  ((or (:start params) (:start defaults) (fn [])))
  (->> (config/system-config (or (:opts params) (:opts defaults) {}))
       (ig/prep)
       (ig/init)
       (reset! system))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& _]
  (start-app))
