(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))
(def workers (atom (hash-map)))

(defn handle-crawl-response
  [res]
  (delay (println "Crawler response received"))
  (swap! queue rest))

(defn work-complete []
  (println "Job's done!"))

(defn drain
  ([q]
   (let [next-task (first q)]
     (swap! queue (fn [current-state]
                    (rest current-state)))
     (drain @queue next-task)))
  ([q next-task]
   (println "Executing next task!")
   ;;(eval ((:queue (next-task)) workers)
   (if (empty? q)
     (println "Job's done!")
     (let [next-task (first q)]
       (swap! queue (fn [current-state]
                      (rest current-state)))
       (drain @queue next-task)))))

(defn push [queue-id task]
  (swap! queue
         (fn [current-state]
           (conj current-state (hash-map :queue queue-id :task task))))
  (drain @queue))

(defn worker [queue-id task-fn]
  (swap! workers
         (fn [current-state]
           (hash-map current-state {queue-id task-fn}))))
