(ns technodrone.queue.core-test
  (:require [clojure.test :refer :all]
            [technodrone.queue.core :refer :all]))

(deftest create-worker
  (testing "Creating a worker returns the work channel."
    (is (satisfies? clojure.core.async.impl.protocols/Channel
                    (worker "creator" '(fn (do)))))
    (close "creator"))

  (testing "Calling a worker returns the work channel."
    (:worker (worker "printer" '(fn [task] (println task))))
    (is (satisfies? clojure.core.async.impl.protocols/Channel (worker "printer")))
    (close "printer")))

(deftest draining-queue
  (testing "Draining a queue."
    (:worker (worker "crawler" '(fn [task] (println task))))
    (push "crawler" 1)
    (push "crawler" 2)
    (push "crawler" 3)
    (:done (drain "crawler"))
    (is (= :done :complete))
    (close "crawler")))
