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
    (worker "saver" (fn [task] (+ 2 task)))
    (push "saver" 3)
    (is (= 5 (drain "saver")))
    (close "saver")))
