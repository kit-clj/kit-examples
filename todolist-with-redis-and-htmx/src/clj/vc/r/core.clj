(ns vc.r.core
  (:require
    [clojure.tools.logging :as log]
    [integrant.core :as ig]
    [vc.r.config :as config]
    [vc.r.env :refer [defaults]]

    ;; Edges
    [kit.edge.cache.redis]
    [kit.edge.templating.selmer]
    [kit.edge.server.undertow]
    [vc.r.web.handler]

    ;; Routes
    [vc.r.web.routes.api]

    [vc.r.web.routes.pages])
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
       (ig/expand)
       (ig/init)
       (reset! system))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& _]
  (start-app))
