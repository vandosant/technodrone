(ns technodrone.queue.core)

(defmacro wait
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

(defmacro enqueue
  ([q concurrent-promise-name concurrent serialized]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~serialized
      ~concurrent-promise-name))
  ([concurrent-promise-name concurrent serialized]
   `(enqueue (future) ~concurrent-promise-name ~concurrent ~serialized)))

(defn push [msg]
  (time @(-> (enqueue queue-id (wait 100 "Message 1 Accepted") (println @queue-id))
             (enqueue queue-id (wait 200 "Message 2 Accepted") (println @queue-id))
             (enqueue queue-id (wait 150 msg) (println @queue-id)))))
