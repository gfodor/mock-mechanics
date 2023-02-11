(ns mockmechanics.core
  (:require [mockmechanics.library.util :as util]
            [mockmechanics.library.vector :as vector]
            [mockmechanics.library.matrix :as matrix]))

(load "transforms-jvm")

(defn get-transform-matrix [transform]
  (let [matrix (float-array (range 16))]
    (.getOpenGLMatrix transform matrix)
    matrix))

(defn axis-angle->quaternion [[ax ay az] angle]
  (let [quat (create-quat-4f)]
    (.set quat (create-axis-angle-4f ax ay az (util/to-radians angle)))
    quat))

(defn quaternion->axis-angle [quaternion]
  (let [axis-angle (create-axis-angle-4f 0 0 0 0)]
    (.set axis-angle quaternion)
    [(.-x axis-angle) (.-y axis-angle)
     (.-z axis-angle) (util/to-degrees (.-angle axis-angle))]))

(defn get-transform-rotation [transform]
  (let [rotation (create-quat-4f)]
    (quaternion->axis-angle (.getRotation transform rotation))))

(defn transform->matrix [transform]
  (vec (get-transform-matrix transform)))

(defn matrix->transform [matrix]
  (let [matrix4f (apply (fn [m00 m10 m20 m30
                             m01 m11 m21 m31
                             m02 m12 m22 m32
                             m03 m13 m23 m33]
                          (create-matrix-4f
                               m00 m01 m02 m03
                               m10 m11 m12 m13
                               m20 m21 m22 m23
                               m30 m31 m32 m33))
                        matrix)]
    (create-transform-from-matrix-4f matrix4f)))

(defn combine-transforms [a b]
  (let [ma (transform->matrix a)
        mb (transform->matrix b)
        m (matrix/multiply ma mb)]
    (matrix->transform m)))

(defn remove-transform [a b]
  (let [ma (transform->matrix a)
        mb (transform->matrix b)
        imb (matrix/get-inverse (float-array mb))
        m (matrix/multiply ma imb)]
    (matrix->transform m)))

(defn make-transform [[x y z] [ax ay az angle]]
  (let [transform (create-transform)
        orientation (create-quat-4f)]
    (.set (.origin transform) (create-vector-3f x y z))
    (.set orientation (axis-angle->quaternion [ax ay az] angle))
    (.setRotation transform orientation)
    transform))

(defn apply-transform [transform point]
  (let [matrix (get-transform-matrix transform)
        vector (float-array (conj (vec point) 1))]
    (vec (butlast (matrix/multiply-vector matrix vector)))))

(defn apply-rotation [transform point]
  (let [rotation (get-transform-rotation transform)
        rotation-transform (make-transform [0 0 0] rotation)]
    (apply-transform rotation-transform point)))

(defn get-transform-position [transform]
  (let [vec (.-origin transform)]
    [(.-x vec) (.-y vec) (.-z vec)]))

(defn get-rotation-component [transform]
  (let [rotation (get-transform-rotation transform)]
    (make-transform [0 0 0] rotation)))

(declare matrix->transform)

(defn get-inverse-transform [transform]
  (let [m (get-transform-matrix transform)
        im (matrix/get-inverse m)]
    (matrix->transform im)))

(def identity-transform (make-transform [0 0 0] [1 0 0 0]))

(defn interpolate-transforms [t1 t2 s]
  (let [p1 (get-transform-position t1)
        p2 (get-transform-position t2)
        r1 (get-transform-rotation t1)
        r2 (get-transform-rotation t2)
        p (vector/interpolate p1 p2 s)
        angle (util/interpolate-values (last r1) (last r2) s)
        r (conj (vec (butlast r1)) angle)]
    (make-transform p r)))
