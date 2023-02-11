(ns mockmechanics.library.util
  (:require [clojure.set :refer [difference union map-invert]]
            [clojure.string :refer [split join]]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]))

(defn reader-for-filename [filename] [])

(defn writer-for-filename [filename] [])

(defn parse-int [s] 0)

(defn parse-float [string] 0)

(defn run-in-thread! [f] (f))

(defn file-exists? [filename] false)

(defn get-files-at [filename] [])

(defn make-color [r g b] [r g b])

(def pi Math/PI)
(def e Math/E)

(defn get-red [color] 0)

(defn get-green [color] 0)

(defn get-blue [color] 0)

(defn get-pixel [image x y] (make-color 0 0 0))

(defn sleep [n])

(defn get-current-time [] 0)

(defn to-radians [angle] 0)

(defn to-degrees [angle] 0)

(defn sin [angle] 0)

(defn cos [angle] 0)

(defn acos [value] 0)

(defn atan2 [y x] 0)

(defn sqrt [x] 0)

(defn abs [x] 0)

(defn pow [n e] 0)

(defn round [n] 0)

(defn delete-temp-files! [])

(defn get-window-coordinates [] [0 0])

(defn create-directory! [name])
