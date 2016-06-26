(ns technodrone.visualization.svg
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))

(defn comparator-over-maps
  [comparison-fn ks]
  (fn [maps]
    (zipmap ks
            (map (fn [k] (apply comparison-fn (map k maps)))
                 ks))))

(def min (comparator-over-maps clojure.core/min [:salary :years-experience]))
(def max (comparator-over-maps clojure.core/max [:salary :years-experience]))

(defn translate-to-00
  [locations]
  (let [mincoords (min locations)]
    (map #(merge-with - % mincoords) locations)))

(defn scale
  [width height locations]
  (let [maxcoords (max locations)
        ratio {:salary (/ height (:salary maxcoords))
               :years-experience (/ width (:years-experience maxcoords))}]
    (map #(merge-with * % ratio) locations)))

(defn datamap->point
  [datamap]
  (str (:salary datamap) "," (:years-experience datamap)))

(defn points
  [locations]
  (s/join " " (map datamap->point locations)))

(defn line
  [points]
  (str "<polyline points=\"" points "\" />"))

(defn transform
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  [width height locations]
  (str "<svg height=\"" height "\" width=\"" width "\">"
       "<g>"
       (-> (transform width height locations)
           points
           line)
       "</g>"
       "</svg>"))
