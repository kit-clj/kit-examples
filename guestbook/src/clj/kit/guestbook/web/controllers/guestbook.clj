(ns kit.guestbook.web.controllers.guestbook
  (:require
   [clojure.tools.logging :as log]
   [kit.guestbook.web.routes.utils :as utils]
   [ring.util.http-response :as http-response]))

(defn save-message!
  [{:keys [form-params] :as req}]
  (log/debug "saving message" form-params)
  (let [{:keys [query-fn]} (utils/route-data req)]
    (try
      (query-fn :save-message (select-keys form-params [:name :message]))
      (http-response/found "/")
      (catch Exception e
        (log/error e "failed to save message!")
        (http-response/found "/error")))))

(defn get-messages
  [req]
  (log/debug "loading messages")
  (let [{:keys [query-fn] :as data} (utils/route-data req)]
    (println "query-fn" query-fn data)
    (query-fn :get-messages {})))