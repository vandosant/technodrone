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

(defn multiply-and-round
  [num1 num2]
  (int (* num1 num2)))

(defn scale
  [width height locations]
  (let [maxcoords (max locations)
        ratio {:salary (/ height (:salary maxcoords))
               :years-experience (/ width (:years-experience maxcoords))}]
    (map #(merge-with multiply-and-round % ratio) locations)))

(defn datamap->point
  [datamap]
  (list (:salary datamap) (:years-experience datamap)))

(defn points
  [locations]
  (map datamap->point locations))

(defn line
  [points]
  (str "<polyline points=\"" points "\" />"))

(defn circle
  [radius point]
  (str "<circle r=\"" radius "\"" "cy=\"" (first point) "\"" "cx=\"" (last point) "\"" "/>"))

(defn circles
  [radius points]
  (s/join " " (map (fn [point]
                     (circle radius point)) points)))

(defn transform
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  [width height locations]
  (str "<svg height=\"" height "\" width=\"" width "\">"
       "<g>"
       (->> (transform width height locations)
           points
           (circles 3))
       "</g>"
       "</svg>"))
