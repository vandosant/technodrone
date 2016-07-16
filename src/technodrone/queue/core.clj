(ns technodrone.queue.core
  (:require [technodrone.crawler.get :refer [fetch-data]]))

(def queue (atom (vector)))
(def workers (atom (hash-map)))
(def finished-work (atom (vector)))

(defn results
  [queue-id]
  @finished-work)

(defn clear-results []
  reset! finished-work (vector))

(defn drain
  ([queue-id]
   (let [next-task (first @queue)]
     (swap! queue (fn [current-state]
                    (rest current-state)))
     (drain queue-id next-task)))
  ([queue-id next-task]
   (println "Executing next task!")
   (let [current-queue @queue]
     (if (empty? current-queue)
       (println "Job's done!")
       (let [next-task (first current-queue)]
         (swap! finished-work (fn [current-state]
                               (conj current-state (->>
                      (get @workers (:queue next-task))
                      (:task next-task)))))
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
           (merge current-state {queue-id task-fn}))))
