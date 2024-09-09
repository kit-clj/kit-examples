(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [clojure.pprint]
   [clojure.spec.alpha :as s]
   [clojure.tools.namespace.repl :as repl]
   [criterium.core :as c]                                  ;; benchmarking
   [expound.alpha :as expound]
   [integrant.core :as ig]
   [integrant.repl :refer [clear go halt prep init reset reset-all]]
   [integrant.repl.state :as state]
   [kit.api :as kit]
   [lambdaisland.classpath.watch-deps :as watch-deps]      ;; hot loading for deps
   [vc.r.core :refer [start-app]]
   [clojure.string :as str]))

;; uncomment to enable hot loading for deps
(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (vc.r.config/system-config {:profile :dev})
                                  (ig/expand)))))

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (vc.r.config/system-config {:profile :test})
                                  (ig/expand)))))

;; Can change this to test-prep! if want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)



(comment
  (go)
  (reset))

(comment
  (require '[kit.edge.cache.redis]
           '[taoensso.carmine :refer [wcar] :as car])
  (def rs (:cache/redis state/system))
  (type rs)
  (redis/inner-config rs)
  (wcar rs "DBSIZE")
  (wcar rs (car/set "foo" "bar"))
  (wcar rs (car/set "baz" "zoo"))
  (wcar rs (car/get "foo"))
  (def ks (wcar rs (car/keys "*")))
  (wcar rs (apply car/mget ks))
  (wcar rs (car/set "todo:1" "Buy milk"))
  (wcar rs (car/set "todo:2" "Buy eggs"))
  (wcar rs (car/set "todo:3" "Cook dinner"))
  (def todo-ks (wcar rs (car/keys "todo:*")))
  (wcar rs (apply car/mget todo-ks))
  (wcar rs (car/del "foo"))

  (wcar rs (apply car/mget todo-ks))

  (wcar rs (car/hset "todo-list" "2" "Buy milk"))
  (wcar rs (car/hset "todo-list" "1" "Buy eggs"))
  (wcar rs (car/hset "todo-list" "3" "Cook pancakes"))
  (wcar rs (car/hgetall "todo-list" ))
  (partition 2 (wcar rs (car/hgetall "todo-list" )))
  (apply hash-map (wcar rs (car/hgetall "todo-list" )))


  (def PREFIX "todo:")
  (defn incr-key [redis-conn]
    (wcar redis-conn (car/incr (str PREFIX "counter"))))

  (defn create-items []
    (doseq [_ (range 3)]
      (let [key (incr-key rs)
            n (str PREFIX key)]
        (wcar rs (car/set n "buy something")))))

  (wcar rs (car/flushall))
  (->> (wcar rs (car/keys (str PREFIX "*")))
       (remove (fn [k] (= k (str PREFIX "counter"))))
       (map (fn [k] [k (wcar rs (car/get k))])))

  (let [ks (->>
            (wcar rs (car/keys (str PREFIX "*")))
            (remove #(= % (str PREFIX "counter")))
            )]
    (zipmap
     ks
     (wcar rs (apply car/mget ks))))

  (wcar rs (car/hgetall "todo-list"))
  (wcar rs (car/get "todo-list:counter"))
  (wcar rs (car/incr "todo-list:counter"))

  (wcar rs (car/hdel "todo-list" nil))



  )
