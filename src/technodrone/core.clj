(ns technodrone.core)
(def filename "Salaries.csv")
(def job-keys [:employer :salary])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:employer identity
                  :salary str->int})

(defn convert
  [job-key value]
  ((get conversions job-key) value))

(defn parse
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))
