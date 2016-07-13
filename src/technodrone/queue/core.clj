(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))
(def workers (atom (list)))

(defn handle-crawl-response
  [res]
  (delay (println "Crawler response received"))
  (swap! queue rest))

(defn work-complete []
  (println "Job's done!"))

(add-watch queue :queue-push-alert
  (fn push-alert
    [key watched old-state new-state]
      (println "State: " new-state)))

(defn push [queue-id task]
  (swap! queue
         (fn [current-state]
           (conj current-state (hash-map :queue queue-id :task task)))))

(defn worker [queue-id task-fn]
  (swap! workers
         (fn [current-state]
           (conj current-state {queue-id task-fn}))))
