(ns mockmechanics.library.matrix)

(defn get-rotation [angle x y z] [])

(defn get-look-at [[eye-x eye-y eye-z]
                   [look-x look-y look-z]
                   [up-x up-y up-z]] [])

(defn multiply [a b] [])

(defn multiply-vector [m v] [])

(defn get-perspective [fovy aspect near far] [])

(defn get-inverse [m] [])

(defn get-transpose [m] [])
