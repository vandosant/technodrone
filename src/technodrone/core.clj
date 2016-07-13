(ns technodrone.core
  (:require [clojure.java.browse :as browse]
            [clojure.data.json :as json]
            [technodrone.visualization.svg :refer [xml]]
            [technodrone.queue.core :refer [worker push]])
  (:gen-class))

(def job-keys [:timestamp :employer :location :job-title :years-employed
               :years-experience :salary])

(defn str->int
  [str]
  (let [nums-in-str (re-find #"\d+" str)]
  (if (clojure.string/blank? nums-in-str)
    nil
    (read-string nums-in-str))))

(defn pad-salary
  [str]
  (let [nums-in-str (re-find #"\d+" str)]
    (if (< (count nums-in-str) 5)
      (str->int "")
      (str->int nums-in-str))))

(def conversions {:timestamp identity
                  :employer identity
                  :location identity
                  :job-title identity
                  :years-employed str->int
                  :years-experience str->int
                  :salary pad-salary
                  })

(defn convert
  [job-key value]
  ((get conversions job-key) value))

(defn parse
  [string]
  (map (fn [row]
         (clojure.string/split
           (clojure.string/replace row #"(\".*)(,)(.*\")" "$1&44$3")
           #","))
       (clojure.string/split string #"\n")))

(defn normalize
  [row]
  (if (> (count row) (count job-keys))
    row
    row))

(defn mapify
  [rows]
  (map (fn [row]
           (reduce (fn [row-map [job-key value]]
                     (assoc row-map job-key (convert job-key value)))
                   {}
                   (map vector job-keys (normalize row))))
       (rest rows)))

(defn template
  [contents]
  (str "<style>polyline { fill:none; stroke: #ceecee; stroke-width:3}</style>"
       contents))

(defn get-graph []
  (let [output-filename "graph.html"
        data-filename "Salaries.csv"]
    (->> (filter #(and (:salary %)
                       (< (:salary %) 300000)
                       (:years-experience %))
                 (mapify (parse (slurp data-filename))))
         (xml 500 500)
         template
         (spit output-filename))))

(defn -main
  [& args]
    (worker "crawler" '(fn [task] (println task)))
    (push "crawler" (json/write-str {:url "http://api.indeed.com/ads/apisearch"
                                   :params "q=web+developer&format=json&v=2&publisher=PUBLISHER_ID"})))
;;    (println "req 2")
;;    (push "crawler" (json/write-str {:url "https://remoteok.io/index.json"})))
