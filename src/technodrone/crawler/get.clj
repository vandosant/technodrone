(ns technodrone.crawler.get
  (:require [aleph.http :as http]
            [byte-streams :as bs]))

(defn fetch-data [publisher]
  ;;(-> @(http/get "https://remoteok.io/index.json")
  ;;    :body
  ;;    bs/to-string
  (-> @(http/get (str "http://api.indeed.com/ads/apisearch?q=web+developer&format=json&v=2&publisher=" publisher))
      :body
      bs/to-string
      println))
