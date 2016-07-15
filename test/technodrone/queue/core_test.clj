(ns technodrone.queue.core-test
  (:require [clojure.test :refer :all]
            [technodrone.queue.core :refer :all]))

(deftest create-worker
  (testing "Creating a worker."
    (is (= nil
           (:worker
             (worker "crawler" '(fn [task] (println task)))))))

  (testing "Using a worker."
    (:worker (worker "crawler" '(fn [task] (println task))))
    (is (= '({:task 1, :queue "crawler"})
           (push "crawler" 1)))))

(deftest draining-queue
  (testing "Draining a queue."
    (:worker (worker "tester" '(fn [task] (println task))))
    (push "crawler" 1)
    (push "crawler" 2)
    (push "crawler" 3)
    (is (= 3
           (length "tester")))
    (drain "crawler")
    (is (= 0
           (length "tester")))))
