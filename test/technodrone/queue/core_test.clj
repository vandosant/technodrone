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
    (is (= nil
           (push "crawler" 1)))))
