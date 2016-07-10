(ns technodrone.crawler.get
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]))

(defn fetch-data [request-json]
  ;;(-> @(http/get "https://remoteok.io/index.json")
  (let [request (json/read-str request-json)]
        (http/get (str (get request "url") "?" (get request "params")) {:timeout 200}
                  (fn [{:keys [status headers body error]}]
                    (if error
                      (println "Failure: " error)
                      (println "Response: " status))))))
