(ns kit.guestbook.web.controllers.guestbook
  (:require
   [clojure.tools.logging :as log]
   [kit.guestbook.web.routes.utils :as utils]
   [ring.util.http-response :as http-response]))

(defn save-message!
  [{{:strs [name message]} :form-params :as request}]
  (log/debug "saving message" name message)
  (let [{:keys [query-fn]} (utils/route-data request)]    
    (try      
      (if (or (empty? name) (empty? message))
        (cond-> (http-response/found "/")
          (empty? name)
          (assoc-in [:flash :errors :name] "name is required")
          (empty? message)
          (assoc-in [:flash :errors :message] "message is required"))
        (do
          (query-fn :save-message! {:name name :message message})
          (http-response/found "/")))
      (catch Exception e
        (log/error e "failed to save message!")
        (-> (http-response/found "/")
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))