(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))

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
           (conj current-state (hash-map queue-id msg))))
  (println queue))
