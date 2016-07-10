(ns technodrone.crawler.get
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

(defn handle-response [{:keys [status headers body error]}]
    (if error
      (println "Failure: " error)
      (println "Response: " status)))


(defn fetch-data [request-json]
  (let [request (json/read-str request-json)]
    (http/get (str (get request "url") "?" (get request "params")) {:timeout 500})))
