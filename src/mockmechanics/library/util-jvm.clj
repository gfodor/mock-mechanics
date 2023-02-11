(ns mockmechanics.library.util
  (:require [clojure.set :refer [difference union map-invert]]
            [clojure.string :refer [split join]])
  (:import java.awt.Color
           java.io.File))

(defn reader-for-filename [filename]
  (clojure.java.io/reader filename))

(defn writer-for-filename [filename]
  (clojure.java.io/writer filename))

(defn parse-int [s]
  (Integer/parseInt s))

(defn parse-float [string]
  (try
    (Float/parseFloat string)
    (catch Exception e nil)))

(defn run-in-thread! [f]
  (let [thread (Thread. f)]
    (.start thread)
    thread))

(defn file-exists? [filename]
  (.exists (clojure.java.io/file filename)))

(defn get-files-at [filename]
  (let [directory (clojure.java.io/file filename)]
    (map #(.getName %)
         (filter #(.isFile %) (file-seq directory)))))

(def colors {:medium-gray (new Color 128 128 128)
             :gray (new Color 128 128 128)
             :orange (new Color 255 102 0)
             :white (new Color 255 255 255)
             :light-gray (new Color 179 179 179)
             :yellow (new Color 255 255 0)
             :green (new Color 0 255 0)
             :dark-red (new Color 128 0 0)
             :dark-yellow (new Color 255 204 0)
             :dark-gray (new Color 51 51 51)
             :red (new Color 255 0 0)
             :blue (new Color 0 0 255)
             :dark-green (new Color 0 145 0)
             :dark-blue (new Color 0 0 128)
             :almost-black (new Color 10 10 10)
             :pink (new Color 255 0 255)
             :teal (new Color 170 212 0)
             :purple (new Color 128 0 175)
             :beige (new Color 170 136 0)
             :black (new Color 0 0 0)})

(defn make-color [r g b]
  (new Color r g b))

(def pi Math/PI)
(def e Math/E)

(defn get-red [color]
  (.getRed color))

(defn get-green [color]
  (.getGreen color))

(defn get-blue [color]
  (.getBlue color))

(defn get-pixel [image x y]
  (new Color (.getRGB image x y)))

(defn sleep [n]
  (Thread/sleep n))

(defn get-current-time []
  (System/currentTimeMillis))

(defn to-radians [angle]
  (Math/toRadians angle))

(defn to-degrees [angle]
  (Math/toDegrees angle))

(defn sin [angle]
  (Math/sin (to-radians angle)))

(defn cos [angle]
  (Math/cos (to-radians angle)))

(defn acos [value]
  (to-degrees (Math/acos value)))

(defn atan2 [y x]
  (Math/toDegrees (Math/atan2 y x)))

(defn sqrt [x]
  (Math/sqrt x))

(defn abs [x]
  (Math/abs x))

(defn pow [n e]
  (Math/pow n e))

(defn round [n]
  (Math/round (float n)))
