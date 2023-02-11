(ns mockmechanics.core
  (:require [mockmechanics.library.util :refer :all]
            [mockmechanics.library.matrix :as matrix]
            [mockmechanics.library.vector :as vector]))

(defn reset-out! [])

(defn gl-println [& forms]
  (println forms))

(defn init-graphics! [])

(defn clear-viewport! [])

(defn clear-depth-buffer! [])

(defn should-close? [window] false)

(defn poll-events [])

(defn swap-buffers [window])

(defn set-viewport [width height])

(defn create-key-handler! [window])

(defn create-mouse-handler! [window])

(defn create-mouse-motion-handler! [window])

(defn create-mouse-scroll-handler! [window])

(defn create-window-size-handler! [window])

(defn create-window-focus-handler! [window])

(defn set-title! [text] )

(defn set-window-size! [window width height] )

(defn window-init-and-run! [loop!]
    (loop! []))

(defn check-shader [shader])

(defn check-program [program])

(defn load-shader [filename type] {})

(defn compile-program [vertex-filename fragment-filename] {})

(defn make-int-buffer [size] [])

(defn get-attribute-names [index] [])

(defn get-uniform-names [index] [])

(defn create-program [base-name]
    {:index 0
     :uniforms []
     :attributes []})

(defn new-image [width height] [])

(defn open-image [filename] (new-image))

(defn get-image-width [image] 0)

(defn get-image-height [image] 0)

(defn get-image-graphics [image] {})

(defn clear
  ([image])
  ([image color]))

(defn draw-pixel [image color x y] image)

(defn fill-rect [image color x y w h])

(defn draw-rect [image color x y w h])

(defn fill-circle [image color x y r])

(defn draw-circle [image color x y r])

(defn get-text-width! [text size] 10)

(defn draw-text [image color text x y size])

(defn draw-text-mono [image color text x y size])

(defn draw-ellipse [image color rect])

(defn draw-line [image color x1 y1 x2 y2])

(defn fill-polygon [image color points])

(defn draw-polygon [image color points])

(defn draw-image [image image2 x y & corner])

(defn image->buffer [image] [])

(defn reset-texture [mesh] mesh)

(defn set-texture [mesh] mesh)

(defn get-float-buffer [seq] [])

(defn draw-lighted-mesh! [world mesh transform])

(defn draw-colored-mesh! [world mesh transform])

(defn draw-textured-mesh! [world mesh transform])

(defn draw-lines! [world mesh transform])

(defn draw-ortho-mesh! [world mesh])

(defn gen-textures [] )
