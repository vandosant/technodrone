(ns technodrone.core)
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
                  :years-employed identity
                  :years-experience identity
                  :salary identity
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
