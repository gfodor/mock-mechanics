(ns mockmechanics.library.matrix)

(defn get-identity []
  (float-array [1 0 0 0
                0 1 0 0
                0 0 1 0
                0 0 0 1]))

(defn get-scale [x y z]
  (float-array [x 0 0 0
                0 y 0 0
                0 0 z 0
                0 0 0 1]))

(load "matrix-js")
