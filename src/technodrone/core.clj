(ns technodrone.core
  (:require [clojure.java.browse :as browse]
            [technodrone.visualization.svg :refer [xml]])
  (:gen-class))
(def filename "Salaries.csv")
(def job-keys [:timestamp :employer :location :job-title :years-employed
               :years-experience :salary])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:timestamp identity
                  :employer identity
                  :location identity
                  :job-title identity
                  :years-employed str->int
                  :years-experience str->int
                  :salary str->int
                  })

(defn convert
  [job-key value]
  ((get conversions job-key) value))

(defn parse
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn normalize
  [row]
  (if (> (count row) 12)
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

(defn -main
  [& args]
  (let [filename "graph.html"]
    (->> (mapify (parse (slurp filename)))
         (xml 50 100)
         template
         (spit filename))))
