
(ns temp.core)

(defn get-tree-with-root [parts root-name]
  (let [root (get-in parts [root-name])
        children (keys (:children root))
        descendents (map #(get-tree-with-root parts %)
                         children)]
    (vec (apply concat [root-name] descendents))))

(defn translate-mode-pressed [world {:keys [x y]}]
  (if-let [collision (get-collision world x y)]
    (if (:control-pressed world)
      (assoc-in world [:selected-part] (:part-name collision))
      (if-let [selected-part (:selected-part world)]
        (let [old-parent-name (get-parent-part world selected-part)
              new-parent-name (:part-name collision)
              new-parent (get-in world [:parts new-parent-name])
              subtree (get-tree-with-root (:parts world) selected-part)]
          (if (and (can-place-part-at? world collision)
                   (not (in? new-parent-name subtree)))
            (-> world
                (dissoc-in [:parts old-parent-name :children selected-part])
                (place-part-at selected-part collision)
                (move-part-pressed selected-part nil))
            world))
        world))
    world))

(defn translate-mode-moved [world event]
  (move-part-moved world event))

(defn translate-mode-released [world event]
  (move-part-released world event))
