(ns kit.guestbook.web.controllers.guestbook
  (:require
   [clojure.tools.logging :as log]
   [kit.guestbook.web.routes.utils :as utils]
   [ring.util.http-response :as http-response]))

(defn get-messages
  [request]
  (log/debug "loading messages")
  (let [{:keys [query-fn]} (utils/route-data request)]
    (println query-fn)
    (query-fn :get-messages {})))

(defn save-message!
  [{:keys [form-params] :as request}]
  (log/debug "saving message" form-params)
  (let [{:keys [query-fn]} (utils/route-data request)]
    (try
      (query-fn :save-message (select-keys form-params [:name :message]))
      (http-response/found "/")
      (catch Exception e
        (log/error e "failed to save message!")
        (http-response/found "/error")))))