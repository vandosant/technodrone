(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

(def queues (atom (hash-map)))
(def workers (atom (hash-map)))

(defn drain [queue-id]
  (let [task (<!! (@queues queue-id))]
    (if task
      ((@workers queue-id) task)
      false)))

(defn push [queue-id task]
  (go (>!! (@queues queue-id) task)))

(defn close [queue-id]
  (close! (@queues queue-id)))

(defn worker
  ([queue-id]
   (@queues queue-id))
  ([queue-id task-fn]
    (swap! workers assoc queue-id task-fn)
    (swap! queues assoc queue-id (chan))
    (@queues queue-id)))
