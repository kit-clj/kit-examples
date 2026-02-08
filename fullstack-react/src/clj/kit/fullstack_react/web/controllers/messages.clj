(ns kit.fullstack-react.web.controllers.messages
  (:require
    [kit.fullstack-react.web.routes.utils :as utils]
    [ring.util.http-response :as http-response]))

(defn list-messages
  [req]
  (let [{:keys [query-fn]} (utils/route-data req)
        messages (query-fn :get-messages {})]
    (http-response/ok messages)))

(defn create-message!
  [req]
  (let [{:keys [query-fn]} (utils/route-data req)
        {:keys [author body]} (:body-params req)]
    (query-fn :create-message! {:author author :body body})
    (http-response/created {:message "Message created"})))
