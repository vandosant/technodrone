(ns technodrone.core
  (:require [clojure.java.browse :as browse]
            [technodrone.visualization.svg :refer [xml]])
  (:gen-class))

(def job-keys [:timestamp :employer :location :job-title :years-employed
               :years-experience :salary])

(defn str->int
  [str]
  (let [nums-in-str (re-find #"\d+" str)]
  (if (clojure.string/blank? nums-in-str)
    nil
    (bigint nums-in-str))))

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

(defn -main
  [& args]
  (let [output-filename "graph.html"
        data-filename "Salaries.csv"]
    (->> (filter #(and (:salary %)
                       (:years-experience %))
                 (mapify (parse (slurp data-filename))))
         (xml 50 100)
         template
         (spit output-filename))))
