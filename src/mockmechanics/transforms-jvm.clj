(ns mockmechanics.core
  (:require [mockmechanics.library.util :as util]
            [mockmechanics.library.vector :as vector]
            [mockmechanics.library.matrix :as matrix])
  (:import javax.vecmath.Matrix4f
           javax.vecmath.Quat4f
           javax.vecmath.Vector3f
           javax.vecmath.AxisAngle4f
           com.bulletphysics.linearmath.Transform))

(defn create-matrix-4f [m00 m01 m02 m03
                         m10 m11 m12 m13
                         m20 m21 m22 m23
                         m30 m31 m32 m33]
  (new Matrix4f
       m00 m01 m02 m03
       m10 m11 m12 m13
       m20 m21 m22 m23
       m30 m31 m32 m33))

(defn create-vector-3f [x y z]
  (new Vector3f x y z))

(defn create-quat-4f [] (new Quat4f))

(defn create-axis-angle-4f [x y z angle]
  (new AxisAngle4f x y z angle))

(defn create-transform []
  (new Transform))

(defn create-transform-from-matrix-4f [m]
  (Transform. m))
