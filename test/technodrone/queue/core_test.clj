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
    (worker "printer" '(fn [task] (println task)))
    (is (satisfies? clojure.core.async.impl.protocols/Channel (worker "printer")))
    (close "printer")))

(deftest draining-queue
  (testing "Returns the task."
    (worker "crawler" '(fn [task] (println task)))
    (push "crawler" 3)
    (is (= 3 (drain "crawler")))
    (close "crawler"))

  (testing "Runs the task."
    (worker "saver" '(fn [task] (spit "/tmp/draining_q_test" task)))
    (push "saver" "test-task")
    (drain "saver")
    (is (= "test-task" (slurp "/tmp/draining_q_test")))
    (close "saver")
    (io/delete-file "/tmp/draining_q_test")))
