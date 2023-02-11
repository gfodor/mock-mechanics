(ns mockmechanics.core
  (:require [mockmechanics.library.util :refer :all]
            [mockmechanics.library.matrix :as matrix]
            [mockmechanics.library.vector :as vector])
  (:import java.lang.System
           java.io.OutputStreamWriter))

(declare draw-2d!)
(declare draw-3d!)
(declare draw-ortho-mesh!)
(declare create-ortho-mesh)
(declare redraw)

(defn create-world [])
(defn draw-world! [world])
(defn update-world [world elapsed] world)
(defn key-pressed [world event] world)
(defn key-released [world event] world)
(defn mouse-pressed [world event] world)
(defn mouse-moved [world event] world)
(defn mouse-released [world event] world)
(defn mouse-scrolled [world event] world)
(defn window-changed [world event] world)
(defn window-focused [world focused] world)
(defn keep-active? [world] false)

(def window-width (atom 800))
(def window-height (atom 600))
(def time-since-update (atom 0))
(def the-window (atom nil))

(def mouse-x (atom 0))
(def mouse-y (atom 0))
(def mouse-button (atom nil))

(def out (atom nil))

(defn get-button-name [value]
  (get {0 :left
        1 :right
        2 :middle} value))

(defn location-name->keyword [name]
  (keyword (apply str (clojure.string/replace name #"_" "-"))))

(load "window-jvm")

(defn recompute-viewport [world width height]
  (let [projection-matrix (matrix/get-perspective
                            10 (/ width height) 3 1000)
        world (-> world
                  (assoc-in [:projection-matrix] projection-matrix)
                  (assoc-in [:ortho-mesh] (create-ortho-mesh width height))
                  (assoc-in [:window-width] width)
                  (assoc-in [:window-height] height))]
    (set-viewport width height)
    (reset! window-width width)
    (reset! window-height height)
    (redraw world)))

(def to-run (atom nil))

(defn run-pending! []
  (swap! to-run (fn [tr]
                  (when (not (nil? tr))
                    (tr))
                  nil)))

(def last-time (atom (get-current-time)))

(defn chip-active? [world chip-name]
  (let [elapsed 16
        dt (float (/ elapsed 1000))
        chip (get-in world [:parts chip-name])]
    (and
      (not (empty? (:functions chip)))
      (not (>= (:time chip) (+ (:final-time chip) dt))))))

(declare get-parts-with-type)

(defn any-chip-active? [world]
  (let [chip-names (get-parts-with-type (:parts world) :chip)]
    (some #(chip-active? world %) chip-names)))

(declare motherboard-activation-count)
(declare spheres-moving?)
(declare is-avatar-active?)

(def avatar-active-time (atom 10000))

(defn update-and-draw! [window]
  (try
    (run-pending!)
    (catch Exception e))

  (if (or (< @time-since-update 200)
          (< @avatar-active-time 10000)
          (keep-active? @world))
    (let [current-time (get-current-time)
          elapsed (within (- current-time @last-time) 0 40)]

      (reset! last-time current-time)
      (clear-viewport!)
      (try
        (draw-world! @world)
        (catch Exception e))
      (try
        (swap! world (fn [w] (update-world w elapsed)))
        (catch Exception e))

      (swap-buffers window)

      (swap! time-since-update #(+ elapsed %))

      ;; (sleep 100)

      (when (and (in? (:mode @world) [:simulation :graph
                                      :motherboard :property
                                      :avatar])
                 (or
                   (any-chip-active? @world)
                   (> @motherboard-activation-count 0)))
        (reset! time-since-update 0))
      )
    (sleep 5))

  (poll-events))

(defn loop! [window]
  (try
    (reset! world (create-world))
    (catch Exception e))

  (reset! time-since-update 0)

  (while (not (should-close? window))
    (update-and-draw! window)))

(declare place-elements)
(declare create-input-indicator)

(defmacro gl-thread [form]
  `(reset! to-run (fn [] ~form)))

(defn set-recording! []
  (set-window-size! @the-window 800 600)
  (gl-thread
    (update-thing!
      []
      (fn [world]
        (-> (create-world)
            (assoc-in [:recording] true)
            (assoc-in [:show-hints] false)
            (assoc-in [:num-lines] 1)
            (create-input-indicator 670)
            (place-elements)
            (redraw))))))

(defn clear!
  ([]
   (let [mesh (get-in @world [:ortho-mesh])]
     (clear (:image mesh))
     (gl-thread (reset-texture mesh))))
  ([color]
   (let [mesh (get-in @world [:ortho-mesh])]
     (clear (:image mesh) color)
     (gl-thread (reset-texture mesh)))))

(defn draw-pixel! [color x y]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-pixel (:image mesh) color x y)
    (gl-thread (reset-texture mesh))))

(defn fill-rect! [color x y w h]
  (let [mesh (get-in @world [:ortho-mesh])]
    (fill-rect (:image mesh) color x y w h)
    (gl-thread (reset-texture mesh))))

(defn draw-rect! [color x y w h]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-rect (:image mesh) color x y w h)
    (gl-thread (reset-texture mesh))))

(defn fill-circle! [color x y r]
  (let [mesh (get-in @world [:ortho-mesh])]
    (fill-circle (:image mesh) color x y r)
    (gl-thread (reset-texture mesh))))

(defn draw-circle! [color x y r]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-circle (:image mesh) color x y r)
    (gl-thread (reset-texture mesh))))

(defn draw-text! [color text x y size]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-text (:image mesh) color text x y size)
    (gl-thread (reset-texture mesh))))

(defn draw-text-in-box! [text color size box]
  (let [w (* 0.5 (get-text-width! text size))]
    (draw-text! color text (- (:x box) w) (+ (:y box) 5) size)))

(defn draw-ellipse! [color rect]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-ellipse (:image mesh) color rect)
    (gl-thread (reset-texture mesh))))

(defn draw-line! [color x1 y1 x2 y2]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-line (:image mesh) color x1 y1 x2 y2)
    (gl-thread (reset-texture mesh))))

(defn fill-polygon! [color points]
  (let [mesh (get-in @world [:ortho-mesh])]
    (fill-polygon (:image mesh) color points)
    (gl-thread (reset-texture mesh))))

(defn draw-polygon! [color points]
  (let [mesh (get-in @world [:ortho-mesh])]
    (draw-polygon (:image mesh) color points)
    (gl-thread (reset-texture mesh))))

(defn draw-image! [image2 x y & corner]
  (let [mesh (get-in @world [:ortho-mesh])]
    (apply draw-image (:image mesh) image2 x y corner)
    (gl-thread (reset-texture mesh))
    nil))

(def redraw-flag (atom true))

(defn draw-world! [world]
  (try
    (draw-3d! world)
    (catch Exception e))

  (when @redraw-flag
    (try
      (draw-2d! world)
      (catch Exception e))
    (reset! redraw-flag false))

  (clear-depth-buffer!)
  (draw-ortho-mesh! world (:ortho-mesh world))
  )

(defn redraw! []
  (reset! time-since-update 0)
  (reset! redraw-flag true))

(defn redraw [world]
  (reset! time-since-update 0)
  (reset! redraw-flag true)
  world)

(defn -main [& args]
  (reset-out!)
  (window-init-and-run! loop!)
  nil
  )

(defn create-base-world []
  (init-graphics!)

  (-> {}
      (assoc-in [:programs :basic] (create-program "basic"))
      (assoc-in [:programs :flat] (create-program "flat"))
      (assoc-in [:programs :textured] (create-program "textured"))
      (assoc-in [:programs :ortho] (create-program "ortho"))
      (assoc-in [:programs :colored] (create-program "colored"))
      (assoc-in [:programs :animated] (create-program "animated"))
      (recompute-viewport @window-width @window-height)
      (#(create-camera % [0 0 1] 40 25 -35))
      (compute-camera)))

(defn reset-world! []
  (gl-thread
    (try
      (reset! world (create-world))
      (catch Exception e))))

(defn set-mesh-position [mesh position]
  (let [transform (:transform mesh)
        rotation (get-transform-rotation transform)]
    (assoc-in mesh [:transform] (make-transform position rotation))))

(defn set-mesh-rotation [mesh rotation]
  (let [transform (:transform mesh)
        position (get-transform-position transform)]
    (assoc-in mesh [:transform] (make-transform position rotation))))

(defn set-mesh-color [mesh color]
  (let [color (get-color color)
        r (/ (get-red color) 255)
        g (/ (get-green color) 255)
        b (/ (get-blue color) 255)]
    (assoc-in mesh [:color] [r g b 1.0])))

(defn compute-normals [vertices]
  (flatten (map (fn [[a b c]]
                  (let [v1 (vector/subtract b a)
                        v2 (vector/subtract c a)
                        v3 (vector/cross-product v1 v2)
                        nv3 (vector/normalize v3)]
                    (list nv3 nv3 nv3)))
                (partition 3 (partition 3 vertices)))))

(defn quaternion-from-normal [normal]
  (let [normal (vector/normalize normal)]
    (cond
      (vector/equal? normal [0 1 0]) [0 1 0 0]
      (vector/equal? normal [0 -1 0]) [1 0 0 180]
      :else (let [axis (vector/cross-product [0 1 0] normal)
                  angle (vector/angle normal [0 1 0])]
              (conj axis angle)))))

(defn get-cube-vertices []
  (let [corners [[-0.5 0.5 0.5] [0.5 0.5 0.5] [-0.5 -0.5 0.5] [0.5 -0.5 0.5]
                 [-0.5 0.5 -0.5] [0.5 0.5 -0.5] [-0.5 -0.5 -0.5] [0.5 -0.5 -0.5]]
        indices [2 3 0   3 1 0   4 7 6   4 5 7
                 2 0 6   6 0 4   7 1 3   5 1 7
                 0 1 5   0 5 4   7 3 2   6 7 2]]
    (vec (flatten (map (fn [index]
                         (nth corners index)) indices)))))

(defn get-cube-texture-coordinates []
  (let [a1 (/ 1 3)
        a2 (* 2 a1)
        a3 (* 3 a1)
        b1 0.5
        b2 1.0]
    [ 0 b1   a1 b1    0  0   a1 b1   a1  0    0  0
     a2  0   a1 b1   a2 b1   a2  0   a1  0   a1 b1
     a3 b1   a3  0   a2 b1   a2 b1   a3  0   a2  0
     a1 b2    0 b1    0 b2   a1 b1    0 b1   a1 b2
     a1 b2   a2 b2   a2 b1   a1 b2   a2 b1   a1 b1
     a3 b2   a3 b1   a2 b1   a2 b2   a3 b2   a2 b1]))

(defn get-circle-vertices [r y divisions]
  (let [angles (map (fn [i]
                      (* i (/ 360 divisions)))
                    (range divisions))
        vertices (map (fn [angle]
                        [(* r (cos angle)) y (* r (sin angle))])
                      angles)]
    vertices))

(defn create-mesh [vertices position rotation
                   scale skin tex-coords normals]
  (let [scale (if (vector? scale)
                scale
                (vec (repeat 3 scale)))
        vertices (vec (flatten vertices))
        normals (if (empty? normals)
                  (vec (compute-normals vertices))
                  (vec (flatten normals)))
        base-mesh {:vertices vertices
                   :vertices-buffer (get-float-buffer vertices)
                   :normals normals
                   :normals-buffer (get-float-buffer normals)
                   :transform (make-transform position rotation)
                   :scale scale}]
    (cond
      (string? skin)
      (let [texture-id (GL11/glGenTextures)
            tex-coords (vec (flatten tex-coords))]
        (-> base-mesh
            (assoc-in [:draw-fn] draw-textured-mesh!)
            (assoc-in [:program] :textured)
            (assoc-in [:image] (open-image skin))
            (assoc-in [:texture-coordinates] tex-coords)
            (assoc-in [:texture-coordinates-buffer]
                      (get-float-buffer tex-coords))
            (assoc-in [:texture-id] texture-id)
            (set-texture)))

      (sequential? skin)
      (let [colors (vec (flatten skin))]
        (-> base-mesh
            (assoc-in [:colors] colors)
            (assoc-in [:colors-buffer] (get-float-buffer colors))
            (assoc-in [:draw-fn] draw-colored-mesh!)
            (assoc-in [:program] :colored)))

      :else
      (let [color (get-color skin)
            r (/ (get-red color) 255.0)
            g (/ (get-green color) 255.0)
            b (/ (get-blue color) 255.0)]
        (-> base-mesh
            (assoc-in [:color] [r g b 1.0])
            (assoc-in [:draw-fn] draw-lighted-mesh!)
            (assoc-in [:program] :flat))))))

(defn create-cube-mesh [position rotation scale skin]
  (create-mesh (get-cube-vertices)
               position rotation scale skin
               (get-cube-texture-coordinates)
               []))

(defn find-line [lines start]
  (find-if #(.startsWith % start) lines))

(defn parse-line [line]
  (map read-string (rest (.split line " "))))

(defn parse-material [directory lines]
  (let [name (subs (find-line lines "newmtl") 7)
        texture-line (find-line lines "map_Kd")]
    {name {:diffuse (parse-line (find-line lines "Kd"))
           :texture (if texture-line
                      (str directory "/" (subs texture-line 7)))}}))

(defn parse-materials [filename]
  (let [lines (read-lines filename)
        lines (filter (fn [line]
                        (or (.startsWith line "newmtl")
                            (.startsWith line "Kd")
                            (.startsWith line "map_Kd")))
                      lines)
        directory (subs filename 0 (.lastIndexOf filename "/"))
        materials (create-groups #(.startsWith % "newmtl") lines)]
    (apply merge (cons {"white" {:diffuse [1 1 1]
                                 :texture nil}}
                       (map #(parse-material directory %) materials)))))

(defn parse-line-with-slashes [line]
  (map (fn [item]
         (map read-string (filter (comp not empty?)
                                  (.split item "/"))))
       (rest (.split line " "))))

(defn use-indices [vector indices]
  (let [min-index (apply min indices)
        indices (map #(- % min-index) indices)]
    (map (fn [v]
           (nth vector v))
         indices)))

(defn create-colors [lines materials]
  (let [groups (create-groups #(.startsWith % "usemtl") lines)]
    (apply concat
           (map (fn [group]
                  (let [n (* 3 (count
                                 (filter #(.startsWith % "f ") group)))
                        color (conj (vec (:diffuse
                                          (get materials (subs (first group) 7)))) 1)]
                    (repeat n color)))
                groups))))

(defn create-model-mesh [filename position rotation scale color]
  (with-open [reader (reader-for-filename filename)]
    (let [materials-filename (-> filename
                                 (subs 0 (.lastIndexOf filename "."))
                                 (str ".mtl"))
          materials (parse-materials materials-filename)
          lines (filter (fn [line]
                          (or (.startsWith line "o")
                              (.startsWith line "v")
                              (.startsWith line "vn")
                              (.startsWith line "vt")
                              (.startsWith line "f")
                              (.startsWith line "usemtl")))
                        (line-seq reader))
          v (map parse-line (filter #(.startsWith % "v ") lines))
          n (map parse-line (filter #(.startsWith % "vn") lines))
          t (map parse-line (filter #(.startsWith % "vt") lines))
          faces (mapcat parse-line-with-slashes
                        (filter #(.startsWith % "f") lines))
          vertices (use-indices v (map first faces))
          normals (use-indices n (map last faces))
          texture-name (some :texture (vals materials))
          texture-coords (if texture-name
                           (use-indices t (map #(nth % 1) faces))
                           [])
          texture-coords (map (fn [[u v]]
                                [u (- 1.0 v)])
                              texture-coords)
          skin (or color
                   texture-name
                   (create-colors lines materials))]
      (create-mesh vertices position rotation scale
                   skin texture-coords normals))))

(defn create-line-mesh [a b color]
  (let [vertices (vec (concat a b))
        color (get-color color)
        r (/ (get-red color) 255)
        g (/ (get-green color) 255)
        b (/ (get-blue color) 255)
        line {:vertices-buffer (get-float-buffer vertices)
              :color [r g b 1.0]
              :transform (make-transform [0.0 0.0 0.0] [0 1 0 0])
              :program :basic
              :scale [1 1 1]
              :draw-fn draw-lines!}]
    line))

(defn create-circle-mesh [center normal radius color]
  (let [vertices (vec (flatten
                        (rotate-list
                          (mapcat (fn [x]
                                    [x x])
                                  (get-circle-vertices radius 0.0 40)))))
        color (get-color color)
        r (/ (get-red color) 255)
        g (/ (get-green color) 255)
        b (/ (get-blue color) 255)
        circle {:vertices-buffer (get-float-buffer vertices)
                :color [r g b 1.0]
                :transform (make-transform center
                                           (quaternion-from-normal normal))
                :program :basic
                :scale [1 1 1]
                :draw-fn draw-lines!}]
    circle))

(defn create-path-mesh [vertices color]
  (let [vertices (interleave vertices (rest vertices))
        vertices (vec (flatten vertices))
        color (get-color color)
        r (/ (get-red color) 255)
        g (/ (get-green color) 255)
        b (/ (get-blue color) 255)
        path {:vertices-buffer (get-float-buffer vertices)
              :color [r g b 1.0]
              :transform (make-transform [0 0 0] [1 0 0 0])
              :program :basic
              :scale [1 1 1]
              :draw-fn draw-lines!}]
    path))

(defn draw-mesh! [world mesh]
  (let [draw-fn (:draw-fn mesh)]
    (draw-fn world mesh (:transform mesh))))

(defn get-grid-vertices [num-cells cell-size]
  (let [hw (/ (* cell-size num-cells) 2)
        seq (map (fn [val]
                   (- (* val cell-size) hw))
                 (range (inc num-cells)))
        min (first seq)
        max (last seq)
        z-parallel (mapcat (fn [x]
                             [x 0 min x 0 max])
                           seq)
        x-parallel (mapcat (fn [z]
                             [min 0 z max 0 z])
                           seq)]
    (vec (concat z-parallel x-parallel))))

(defn create-grid-mesh [num-cells size]
  (let [vertices (get-grid-vertices num-cells size)
        color (get-color :black)
        r (/ (get-red color) 255)
        g (/ (get-green color) 255)
        b (/ (get-blue color) 255)]
    {:vertices-buffer (get-float-buffer vertices)
     :color [r g b 1.0]
     :transform (make-transform [0.0 0.0 0.0] [0 1 0 0])
     :program :basic
     :scale [1 1 1]
     :draw-fn draw-lines!}))

(defn create-ortho-mesh [width height]
  (let [image (new-image width height)
        vertices [-1 -1  0   1 -1  0   -1  1  0
                  1 -1  0   1  1  0   -1  1  0]
        texture-coordinates [0 1   1 1   0 0
                             1 1   1 0   0 0]
        texture-id (GL11/glGenTextures)
        mesh {:vertices-buffer (get-float-buffer vertices)
              :image image
              :texture-coordinates-buffer (get-float-buffer texture-coordinates)
              :texture-id texture-id}]
    (set-texture mesh)))

(defn create-wireframe-mesh [vertices position rotation scale color-name]
  (let [color (get-color color-name)
        r (/ (get-red color) 255)
        g (/ (get-green color) 255)
        b (/ (get-blue color) 255)]
    {:vertices-buffer (get-float-buffer vertices)
     :color [r g b 1.0]
     :transform (make-transform position rotation)
     :program :basic
     :scale scale
     :draw-fn draw-lines!}))

(defn create-wireframe-cube [position rotation scale color-name]
  (let [corners [[-0.5 0.5 0.5]
                 [-0.5 -0.5 0.5]
                 [-0.5 0.5 -0.5]
                 [-0.5 -0.5 -0.5]
                 [0.5 0.5 0.5]
                 [0.5 -0.5 0.5]
                 [0.5 0.5 -0.5]
                 [0.5 -0.5 -0.5]]
        indices [0 1 1 3 3 2 2 0
                 4 5 5 7 7 6 6 4
                 0 4 2 6 3 7 1 5]
        vertices (vec (flatten (map (fn [index]
                                      (nth corners index)) indices)))]
    (create-wireframe-mesh vertices position rotation
                           scale color-name)))
