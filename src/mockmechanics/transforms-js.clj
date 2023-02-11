(ns mockmechanics.core
  (:require [mockmechanics.library.util :as util]
            [mockmechanics.library.vector :as vector]
            [mockmechanics.library.matrix :as matrix]))

(defn create-matrix-4f [m00 m01 m02 m03
                         m10 m11 m12 m13
                         m20 m21 m22 m23
                         m30 m31 m32 m33] [])

(defn create-vector-3f [x y z] [])

(defn create-quat-4f [] [])

(defn create-axis-angle-4f [x y z angle] [])

(defn create-transform [] [])

(defn create-transform-from-matrix-4f [m] [])

(defn get-transform-matrix [transform] [])

(defn axis-angle->quaternion [[ax ay az] angle] [])

(defn quaternion->axis-angle [quaternion] [])

(defn get-transform-rotation [transform] [])

(defn make-transform [[x y z] [ax ay az angle]] [])

(defn get-transform-position [transform] [])
