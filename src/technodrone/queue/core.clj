(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))

(defn crawl-and-dequeue
  [job]
  (fetch-data (:msg job))
  (swap! queue rest))

(defn push-alert
  [key watched old-state new-state]
  (let [next-job (first new-state)]
  (if (= (:queue next-job) "crawler")
    (crawl-and-dequeue next-job))))

(add-watch queue :queue-push-alert push-alert)

(defmacro enqueue
  ([q concurrent-promise-name concurrent]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~concurrent-promise-name))
  ([concurrent-promise-name concurrent]
   `(enqueue (future) ~concurrent-promise-name ~concurrent)))

(defn push [queue-id msg]
  (swap! queue
         (fn [current-state]
           (conj current-state (hash-map :queue queue-id :msg msg)))))
