(ns technodrone.queue.core-test
  (:require [clojure.test :refer :all]
            [technodrone.queue.core :refer :all]
            [clojure.java.io :as io]))

(deftest create-worker
  (testing "Creating a worker returns the work channel."
    (is (satisfies? clojure.core.async.impl.protocols/Channel
                    (worker "creator" '(fn (do)))))
    (close "creator"))

  (testing "Calling a worker returns the work channel."
    (worker "printer" (fn [task] (println task)))
    (is (satisfies? clojure.core.async.impl.protocols/Channel (worker "printer")))
    (close "printer")))

(deftest draining-queue
  (testing "Runs the task."
    (worker "adder" (fn [task] (+ 2 task)))
    (push "adder" 3)
    (is (= 5 (drain "adder")))
    (close "adder"))

  (testing "Runs the tasks in order."
    (worker "adder" (fn [task] (+ 1 task)))
    (push "adder" 1)
    (push "adder" 2)
    (is (= 2 (drain "adder")))
    (is (= 3 (drain "adder")))
    (close "adder"))

  (testing "Returns :empty when there are no tasks"
    (worker "adder" (fn [task] (+ 1 task)))
    (is (= :empty (drain "adder")))
    (close "adder")))
