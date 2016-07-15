(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))
(def workers (atom (hash-map)))

(defn drain
  ([queue-id]
   (let [next-task (first @queue)]
     (swap! queue (fn [current-state]
                    (rest current-state)))
     (drain queue-id next-task)))
  ([queue-id next-task]
   (println "Executing next task!")
   (let [current-queue @queue]
     ;;(eval ((:queue (next-task)) workers)
     (if (empty? current-queue)
       (println "Job's done!")
       (let [next-task (first current-queue)]
         (swap! queue (fn [current-state]
                        (rest current-state)))
         (drain queue-id next-task))))))

(defn length
  [queue-id]
  (count @queue))

(defn push [queue-id task]
  (swap! queue
         (fn [current-state]
           (conj current-state (hash-map :queue queue-id :task task)))))

(defn worker [queue-id task-fn]
  (swap! workers
         (fn [current-state]
           (hash-map current-state {queue-id task-fn}))))
