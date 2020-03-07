
(ns temp.core)

(defn rotate-mode-pressed [world event]
  (let [{:keys [x y]} event]
    (if-let [{:keys [part-name point index]}
             (get-part-collision world x y)]
      (let [part (get-in world [:parts part-name])]
        (-> world
            (assoc-in [:edited-part] part-name)
            (assoc-in [:start-point] [x y])
            (assoc-in [:original-transform] (:transform part))
            (create-weld-groups)))
      world)))

(defn rotate-mode-moved [world event]
  (if-let [part-name (:edited-part world)]
    (let [dx (/ (- (:x event) (first (:start-point world))) window-width)
          grain (if (:shift-pressed world)
                  30
                  10)
          angle (snap-value (map-between-ranges dx -1 1 -1000 1000) grain)
          rotation (make-transform [0 0 0] [0 1 0 angle])
          original-transform (:original-transform world)          
          transform (combine-transforms rotation original-transform)]
      (-> world
          (assoc-in [:parts part-name :transform] transform)
          (assoc-in [:rotation-angle] angle)))
    world))

(defn rotate-mode-released [world event]
  (if-let [part-name (:edited-part world)]
    (let [parent-name (get-parent-part world part-name)
          angle (:rotation-angle world)
          rotation (make-transform [0 0 0] [0 1 0 angle])
          world (set-value-0-transform world part-name)
          original-transform (get-in world [:parts part-name :transform])
          transform (combine-transforms rotation original-transform)]
      (-> world
          (assoc-in [:parts part-name :transform] transform)
          (create-relative-transform part-name parent-name)
          (dissoc-in [:edited-part])))
    world))
