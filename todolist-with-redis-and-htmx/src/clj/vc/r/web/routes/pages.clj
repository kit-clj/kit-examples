(ns vc.r.web.routes.pages
  (:require
   [vc.r.web.middleware.exception :as exception]
   [vc.r.web.pages.layout :as layout]
   [vc.r.web.routes.components :as c]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [ring.util.http-response :refer [content-type ok]]
   [taoensso.carmine :refer [wcar] :as car]))

(defn wrap-page-defaults []
  (let [error-page (layout/error-page
                     {:status 403
                      :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page})))

(def storage "todo-list")
(def counter-key (str storage ":counter"))

(defn home
  [{:keys [cache/redis]}
   {:keys [anti-forgery-token]}]
  (let [items (->> (wcar redis (car/hgetall storage))
                   (partition 2))]
    (-> (c/main-page items anti-forgery-token)
        (ok)
        (content-type "text/html; charset=utf-8"))))

(defn delete-item
  [{:keys [cache/redis]}
   {:keys [path-params]}]
  (wcar redis (car/hdel storage (:id path-params)))
  (ok))

(defn add-item
  [{:keys [cache/redis]}
   {:keys [anti-forgery-token form-params]}]
  (let [v (get form-params "value")
        k (or
           (wcar redis (car/get counter-key))
           (wcar redis (car/incr counter-key)))]
    (println "adding new value" v "with key" k)
    (wcar redis (car/hset storage k v))
    (wcar redis (car/incr counter-key))
    (ok (c/item-row k v anti-forgery-token))))

;; Routes
(defn page-routes [opts]
  [["/" {:get (partial home opts)}]
   ["/delete/:id" {:post (partial delete-item opts)}]
   ["/add" {:post (partial add-item opts)}]])

(def route-data
  {:middleware
   [;; Default middleware for pages
    (wrap-page-defaults)
    ;; query-params & form-params
    parameters/parameters-middleware
    ;; encoding response body
    muuntaja/format-response-middleware
    ;; exception handling
    exception/wrap-exception]})

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-selmer! opts)
  (fn [] [base-path route-data (page-routes opts)]))
