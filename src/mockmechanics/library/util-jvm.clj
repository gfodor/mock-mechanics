(ns mockmechanics.library.util
  (:require [clojure.set :refer [difference union map-invert]]
            [clojure.string :refer [split join]]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]])
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

(defn delete-temp-files! []
  (let [files (filter #(.isFile %)
                      (file-seq (io/file "temp")))]
    (doseq [file files]
      (io/delete-file file))))

(defn get-window-coordinates []
  (map parse-int
       (clojure.string/split (:out (sh "./window-coords.sh")) #"\n")))

(defn create-directory! [name]
  (.mkdirs (new File name)))
