(ns temp.core)

(load "world")
(load "util")
(load "vector")
(load "matrix")
(load "analytic-geometry")
(load "xml")
(load "svg")
(load "transforms")
(load "keymap")
(load "window")
(load "meshes")

(load "miscellaneous")
(load "collision")
(load "physics")
(load "weld-optimization")
(load "mechanical-tree")
(load "undo")
(load "persistence")
(load "snap-specs")
(load "value-force")
(load "modes")
(load "commands")

(do
1

(defn create-world []
  (let [world (create-gl-world)
        r 0.2]
    (create-debug-meshes!)
    (-> world
        (assoc-in [:background-meshes :grid] (create-grid-mesh 24 0.5))
        (assoc-in [:info] (create-info))
        (create-ground-part)
        (assoc-in [:walls]
                  (create-cube-mesh [0 -0.1 0] [1 0 0 0] [12 0.2 12]
                                    (make-color 40 40 40)))
        (assoc-in [:graph-box] {:x 343 :y 540
                                :w 685 :h 150
                                :buffer (new-image 685 150)
                                })
        (assoc-in [:cpu-box] {:x 343 :y 540 :w 685 :h 150})
        (update-move-plane)
        (assoc-in [:cursor] (create-cone-mesh [0 -5 0] [1 0 0 0]
                                              [0.05 0.1 0.05] :black))

        (assoc-in [:command] "")
        (assoc-in [:mode] :idle)
        (assoc-in [:bindings] (get-bindings))
        (assoc-in [:current-color] :red)
        (assoc-in [:color-palette]
                  (create-image "resources/colors.svg" 340 590 -1 40))
        (assoc-in [:selected-mesh]
                  (create-wireframe-cube [0 0.52 0] [1 0 0 0]
                                         [0.3001 0.1001 0.3001] :white))
        (assoc-in [:use-weld-groups] true)
        
        (assoc-in [:planet] (create-planet))
        (update-in [:planet] create-ground)
        (assoc-in [:sphere-radius] r)
        (assoc-in [:sphere-mesh] (create-sphere-mesh [0 0 0] [1 0 0 0]
                                                     [r r r] :blue))
        (assoc-in [:spheres] [])
        (assoc-in [:button-mesh] (create-cylinder-mesh [0 0 0] [1 0 0 0]
                                                       [0.2 0.2 0.2] :red))

        (assoc-in [:graph-snap-value] 0.1)

        (assoc-in [:selected-property] 0)
        (assoc-in [:properties] [:free :hidden :physics])

        (reset-undo! [:ground-children :planet :spheres :parts])
        (prepare-tree)
    )))
(reset-world!)
)

(defn update-world [world elapsed]
  (if (in? (:mode world) [:insert :edit])
    world
    (let [world (-> world
                    (set-probe-values)
                    (run-chips elapsed)
                    (apply-force elapsed)
                    (compute-transforms (if (:use-weld-groups world)
                                          :weld-groups
                                          :parts))
                    (cpus-input-changes)
                    )]
      (recompute-body-transforms! world)
      (step-simulation! (:planet world) elapsed)
      world)))

(defn draw-3d! [world]
  (doseq [mesh (vals (:background-meshes world))]
    (draw-mesh! world mesh))

  (doseq [mesh (vals (:meshes world))]
    (draw-mesh! world mesh))

  (if (:use-weld-groups world)
    (doseq [group (vals (:weld-groups world))]
      (draw-mesh! world group))
    (doseq [part (vals (:parts world))]
      (draw-part! world part))
    )

  (if (> (get-in world [:camera :x-angle]) 0)
    (draw-mesh! world (:walls world)))
  
  (if-let [edited-part (:edited-part world)]
    (let [part (get-in world [:parts edited-part])]
      (draw-part! world part)))

  (draw-buttons! world)
  (draw-spheres! world)

  (if (and
       (= (:mode world) :graph)
       (:selected-chip world))
    (draw-mesh! world (:selected-mesh world)))

  (GL11/glClear GL11/GL_DEPTH_BUFFER_BIT)
  (if (inserting? world)
    (draw-mesh! world (:cursor world)))
  (draw-debug-meshes!)
  )

(defn draw-2d! [world]
  (clear!)
  (draw-text-box! world)
  (if-let [fun (get-function (:mode world) :draw)]
    (fun world))
  (draw-output!)
  )

(defn mouse-scrolled [world event]
  (if (and (= (:mode world) :graph)
           (inside-box? (:graph-box world) (:x event) (:y event)))
    (graph-mode-scrolled world event)
    (let [amount (+ 1 (* (:amount event) -0.05))]
      (zoom-camera world amount))))

(defn mouse-pressed [world event]
  (let [x (:x event)
        y (:y event)]
    (cond
      (in? (:button event) [:middle :right])
      (assoc-in world [:last-point] [x y])

      :else
      (mode-mouse-pressed world event))))

(defn mouse-moved [world event]
  (cond
    (not-nil? (:last-point world))
    (cond
      (= (:button event) :right) (mouse-rotate world event)
      (= (:button event) :middle) (mouse-pan world event)
      :else world)

    :else
    (-> world
        (move-cursor event)
        (mode-mouse-moved event))))

(defn mouse-released [world event]
  (let [world (cond
                (not-nil? (:last-point world))
                (-> world
                    (dissoc-in [:last-point])
                    (update-move-plane))

                :else
                (mode-mouse-released world event)
                )]
    (draw-2d! world)
    (-> world
        (prepare-tree)
        (save-checkpoint!)
        )))
