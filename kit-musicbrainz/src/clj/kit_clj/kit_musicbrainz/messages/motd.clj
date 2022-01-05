(ns kit-clj.kit-musicbrainz.messages.motd
  (:require
    [clojure.core.cache :as cache]
    [integrant.core :as ig])
  (:import
    [org.quartz Job]))

;; Internal

(def messages
  ;; credits: Albert Camus (1-3), Douglas Adams (4-6), Simone de Beauvoir (7-9)
  ["Autumn is a second Spring when every leaf is a flower."
   "If the world were clear, art would not exist."
   "It takes time to live. Like any work of art, life needs to be thought about."
   "Time is an illusion. Lunchtime doubly so."
   "There was a point to this story, but it has temporarily escaped the chronicler's mind."
   "In the beginning the Universe was created. This has made a lot of people very angry and been widely regarded as a bad move."
   "To will freedom and to will to disclose being are one and the same choice"
   "What is an adult? A child blown up by age."
   "We must not confuse the present with the past. With regard to the past, no further action is possible."])

(def ^:const message-key "message-of-the-day")
(def ^:const default-ttl (* 60 60 25))                      ;; 25h

(defn set-message!
  [{:keys [cache]} message]
  (cache/miss cache message-key {:val message :ttl default-ttl}))

(defn get-message
  [{:keys [cache]}]
  (cache/lookup cache message-key))

(defn reset-random-motd!
  [ctx]
  (let [msg (rand-nth messages)]
    (set-message! ctx msg)
    msg))

;; Scheduler

(defmethod ig/init-key :messages/cycle-message-job
  [_ ctx]
  (reify Job
    (execute [_ _]
      (reset-random-motd! ctx))))

;; API

(defn latest-message!
  [ctx]
  (or (get-message ctx)
      (reset-random-motd! ctx)))