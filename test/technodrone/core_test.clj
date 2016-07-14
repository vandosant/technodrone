(ns technodrone.core-test
  (:require [clojure.test :refer :all]
            [technodrone.core :refer :all]
            [technodrone.queue.core :refer [worker push]]))

(deftest create-worker
  (testing "Creating a worker."
    (is (= nil
           (:worker
             (worker "crawler" '(fn [task] (println task))))))))
