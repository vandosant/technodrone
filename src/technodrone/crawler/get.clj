(ns technodrone.crawler.get
  (:require [aleph.http :as http]
            [clojure.data.json :as json]
            [byte-streams :as bs]))

(defn fetch-data [request-json]
  ;;(-> @(http/get "https://remoteok.io/index.json")
  ;;    :body
  ;;    bs/to-string
  (let [request (json/read-str request-json)]
    (-> @(http/get (str (get request "url") "?" (get request "params")))
        :body
        bs/to-string
        println)))
