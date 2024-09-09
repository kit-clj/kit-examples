(ns vc.r.web.controllers.todo
  (:require [vc.r.web.controllers.components :as c]
            [ring.util.http-response :refer [content-type ok]]
            [taoensso.carmine :refer [wcar] :as car]))

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
