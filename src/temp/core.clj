
(ns temp.core (:gen-class))

(load "util")
(load "world")
(load "vector")
(load "matrix")
(load "analytic-geometry")
(load "camera")
(load "transforms")
(load "window")
(load "xml")
(load "picture")
(load "keymap")
(load "debug")

(load "synthesizer")
(load "miscellaneous")
(load "output")
(load "parts")
(load "collision")
(load "weld-optimization")
(load "mechanical-tree")
(load "undo")
(load "persistence")
(load "forces")
(load "modes")
(load "commands")
(load "track-loop")
(load "hints")

(do
1

(defn create-world []
  (-> (create-base-world)
      (merge (read-string (str "{" (slurp "settings.clj") "}")))
      (assoc-in [:num-lines] 6)
      (assoc-in [:background-meshes :grid] (create-grid-mesh 24 0.5))
      (assoc-in [:info] (create-info))
      (assoc-in [:parts] {})
      (assoc-in [:parts :ground-part] (create-ground-part))
      (assoc-in [:other-ground]
                (create-cube-mesh [0 -0.1 0] [1 0 0 0] [12 0.2 12]
                                  (make-color 40 40 40)))
      (assoc-in [:graph-box] {:x 343 :y 530
                              :w 685 :h 150
                              :buffer (new-image 685 150)
                              })
      (assoc-in [:motherboard-box] {:x 343 :y 530
                                    :w 685 :h 150
                                    :buffer (new-image 685 150)})
      (assoc-in [:property-box]
                (create-picture "property-menu" 240 340 -1 60))
      (assoc-in [:layer-box] {:x 343 :y 575 :w 480 :h 60})
      (assoc-in [:toggle-box] {:x 343 :y 575 :w 500 :h 60})
      (assoc-in [:visible-layers] [1])
      (assoc-in [:command] "")
      (assoc-in [:bindings] (get-bindings))
      (assoc-in [:current-color] :red)
      
      (assoc-in [:action-menu]
                (create-picture "action-menu" 240 340 40 -1))
      (assoc-in [:mode-menu]
                (create-picture "mode-menu" 240 340 40 -1))
      
      (assoc-in [:color-palette]
                (create-picture "colors" 340 585 -1 40))
      (assoc-in [:add-menu]
                (create-picture "add-menu" 726 675 -1 50))
      (assoc-in [:add-type] :block)

      (assoc-in [:edit-menu]
                (create-picture "edit-menu" 210 575 -1 50))
      (assoc-in [:edit-subcommand] :move)

      (assoc-in [:use-weld-groups] true)
      (assoc-in [:graph-snap-value] 0.05)

      (assoc-in [:graph-menu]
                (create-picture "graph-menu" 210 575 -1 30))

      (assoc-in [:motherboard-menu]
                (create-picture "motherboard-menu" 210 575 -1 30))
      (assoc-in [:selected-property] 0)
      (assoc-in [:properties] [:free :physics :collision :.])

      (create-physics-world)
      (reset-undo! [:parts])
      (assoc-in [:mode] :simulation)

      (assoc-in [:track-head-model]
                (create-cube-mesh [0 -10000 0] [1 0 0 0] 0.2 :white))
      (place-elements)
      (create-weld-groups)

      ;; (assoc-in [:update-cube] ;;###################################
      ;;           (create-cube-mesh [0 0 0] [1 0 0 0] 0.1 :red))
      ))
(reset-world!)
)

(defn update-world [world elapsed]
  ;; (print-transform (get-thing! [:parts :wagon9966 :transform]))
  ;; (println! (rand))
  (cond
    (in? (:mode world) [:simulation :graph :motherboard])
    (let [elapsed 16 ;;######################
          world (-> world
                    (set-probe-values)
                    (apply-forces elapsed)
                    (run-chips elapsed)
                    (compute-transforms (if (:use-weld-groups world)
                                          :weld-groups
                                          :parts))
                    (update-motherboards)
                    )]
      (recompute-body-transforms! world)
      (step-simulation! (:planet world) elapsed)
      world)

    (= (:mode world) :property)
    (-> world
        (apply-forces elapsed)
        (compute-transforms (if (:use-weld-groups world)
                              :weld-groups
                              :parts)))
    :else world))

;; (defn draw-update-cube! [world] ;;#########################
;;   (if-let [mesh (:update-cube world)]
;;     (let [green-value (if (float= (second (:color mesh)) 1.0)
;;                         0.0
;;                         1.0)]
;;       (set-thing! [:update-cube :color 1] green-value)
;;       (GL11/glClear GL11/GL_DEPTH_BUFFER_BIT)
;;       (draw-mesh! world mesh))))

(defn draw-3d! [world]
  (doseq [mesh (vals (:background-meshes world))]
    (draw-mesh! world mesh))
  
  (if (> (get-in world [:camera :x-angle]) 0)
    (draw-mesh! world (:other-ground world)))

  (draw-spheres! world)
  
  (if (:use-weld-groups world)
    (doseq [group (vals (:weld-groups world))]
      (draw-mesh! world group))
    (doseq [[name part] (:parts world)]
      (if (or (= name :ground-part)
              (not (in? (:layer part) (:visible-layers world))))
        nil
        (draw-part! world part))))

  (draw-track-head! world)
  (draw-selection! world)
  (draw-buttons! world)
  (draw-lamps! world)

  ;; (draw-update-cube! world)
  )

(do
1

(defn show-buttons? [world]
  (or
   (= (:show-buttons world) :always)
   (and
    (= (:show-buttons world) :no-sim)
    (not (= (:mode world) :simulation)))))

(defn draw-2d! [world]
  (clear!)

  (when (show-buttons? world)
    (let [{:keys [image x y w h]} (:action-menu world)]
      (fill-rect! (make-color 70 70 70) x y (+ 20 w) (+ 20 h))
      (draw-image! image x y))

    (let [{:keys [image x y w h]} (:mode-menu world)]
      (fill-rect! (make-color 70 70 70) x y (+ 20 w) (+ 20 h))
      (draw-image! image x y))
    )
  
  (if-let [fun (get-function (:mode world) :draw)]
    (fun world))

  (draw-buffer! world)
  (draw-hint! world)
  )
(redraw!))

(defn mouse-scrolled [world event]
  (if (and (= (:mode world) :graph)
           (inside-box? (:graph-box world) (:x event) (:y event)))
    (graph-mode-scrolled world event)
    (let [amount (+ 1 (* (:amount event) -0.05))]
      (zoom-camera world amount))))

(defn action-menu-pressed [world x y]
  (if-let [region (get-region-at (:action-menu world) x y)]
    (let [world (case region
                  :new (-> world
                           (new-file)
                           (tree-changed))
                  :view (view-all-parts world)
                  :save (save-machine-version world)
                  :open (open-machine-version world)
                  :undo (undo! world)
                  :redo (redo! world)
                  :cancel (cancel-action world))]
      (show-hint world :action region))
    world))

(defn mode-menu-pressed [world x y]
  (if-let [region (get-region-at (:mode-menu world) x y)]
    (-> world
        (change-mode region)
        (show-hint :mode region))
    world))

(defn mouse-pressed [world event]
  (let [x (:x event)
        y (:y event)
        world (-> world
                  (assoc-in [:press-time] (get-current-time))
                  (assoc-in [:press-point] [x y]))]
    (cond
      (and
       (show-buttons? world)
       (inside-box? (:action-menu world) x y))
      (action-menu-pressed world x y)

      (and
       (show-buttons? world)
       (inside-box? (:mode-menu world) x y))
      (mode-menu-pressed world x y)

      (and
       (in? (:button event) [:middle :right])
       (not (and (= (:mode world) :graph)
                 (inside-box? (:graph-box world) x y))))
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
    (mode-mouse-moved world event)))

(defn mouse-released [world event]
  (let [elapsed (- (get-current-time) (:press-time world))
        world (if (and (< elapsed 200)
                       (= (:button event) :right)
                       (< (distance (:press-point world)
                                    [(:x event) (:y event)]) 10))
                (set-pivot world event)
                world)]
    (if (not-nil? (:last-point world))
      (dissoc-in world [:last-point])
      (mode-mouse-released world event))))

(defn window-changed [world event]
  (let [{:keys [width height]} event]
    (-> world
        (recompute-viewport width height)
        (place-elements))))

;; (set-thing! [:use-weld-groups] true)
;; (update-thing! [] create-weld-groups)
;; (get-thing! [:use-weld-groups])
;; (println! (:selected-part @world))
;; (set-thing! [:selected-part] :block13950-copy13961)
;; (println! (get-parent-part @world :block10336-copy135749))
;; (get-part-position @world :block13950-copy153348)
;; (set-thing! [:parts :motherboard10102 :activation-count] 0)
;; (set-thing! [:use-weld-groups] true)

;; (do
;; 1

;; (clear-output!)
;; (let [world @world
;;       wagon-name :wagon9255
;;       ]

;;   ;; (set-thing! [:parts wagon-name :loop-fn]
;;   ;;             [[0.0 [0.0 -2.0 0.0]]
;;   ;;              [0.5 [0.0 0.0 0.0]]
;;   ;;              [1.0 [0.0 0.0 1.0]]])

;;   ;; (set-thing! [:parts wagon-name :loop-fn]
;;   ;;             [[0.0 [0.0 -1.0 0.0]]
;;   ;;              [1.0 [0.0 0.0 0.0]]])

;;   ;; (set-thing! [:parts wagon-name :value] 0.5)

;;   (print-transform (get-thing! [:parts wagon-name :transform]))
  
;;   ))

;; (do
;; 1
;; ;; (set-thing! [:weld-groups :ground-part :children :wagon9966]
;; ;;             (make-transform [-0.5 0.35 0.25] [0.0 0.0 -1.0 90.00000250447816]))

;; ;; (set-thing! [:use-weld-groups] true)
;; ;; (set-thing! [:parts :wagon9966 :value] 1.0)
;; ;; (update-thing! [] #(compute-transforms % :weld-groups))
;; ;; (redraw!)
;; )

;; ;; (print-transform (get-thing! [:weld-groups :ground-part :children :wagon9966]))

;; (do
;; 1  
;; (update-thing! [] tree-changed)
;; (redraw!))
