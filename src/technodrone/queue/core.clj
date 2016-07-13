(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))
(def work (atom (vector)))

(defn handle-crawl-response
  [res]
  (delay (println "Crawler response received"))
  (swap! queue rest))

(defn work-complete []
  (println "Job's done!"))

(defn push-alert
  [key watched old-state new-state]
  (let [next-job (first new-state)]
    (if (= (:queue next-job) "crawler")
      (let [new-work (fetch-data (:msg next-job))]
        (swap! work
               (fn [current-work]
                 (conj current-work new-work)))))))

(add-watch queue :queue-push-alert push-alert)

(defn push [queue-id msg]
  (swap! queue
         (fn [current-state]
           (conj current-state (hash-map :queue queue-id :msg msg)))))

(defn worker [queue-id]
  (swap! queue
         (fn [current-state]
           (conj current-state {queue-id []}))))
