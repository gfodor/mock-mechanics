(ns mockmechanics.core
  (:require [mockmechanics.library.util :refer :all]
            [mockmechanics.library.xml :refer :all]
            [clojure.string :refer [split]]))

(load "picture-jvm")

(defn get-svg-dimensions [document]
  (let [[_ _ w h] (split (get-in document [:attrs :viewBox]) #" ")]
    [(parse-float w) (parse-float h)]))

(defn get-svg-aspect-ratio [document]
  (let [w (get-in document [:attrs :width])
        w (parse-float (subs w 0 (- (count w) 2)))
        h (get-in document [:attrs :height])
        h (parse-float (subs h 0 (- (count h) 2)))]
    (/ h w)))

(defn parse-svg-from-map-with-width [document width]
  (let [aspect-ratio (get-svg-aspect-ratio document)
        height (* aspect-ratio width)]
    (parse-svg-from-map document width height)))

(defn parse-svg-from-map-with-height [document height]
  (let [aspect-ratio (get-svg-aspect-ratio document)
        width (* (/ 1 aspect-ratio) height)]
    (parse-svg-from-map document width height)))

(defn parse-svg-with-width [filename width]
  (let [document (read-xml filename)]
    (parse-svg-from-map-with-width document width)))

(defn parse-hex-color [str]
  [(parse-int (subs str 0 2) 16)
   (parse-int (subs str 2 4) 16)
   (parse-int (subs str 4 6) 16)])

(defn get-svg-regions [document]
  (let [[dx dy] (get-svg-dimensions document)
        g (get-child-if document :g {:inkscape:label "regions"})
        [tx ty] (if-let [translation (get-in g [:attrs :transform])]
                  (let [[tx ty] (split (apply str (butlast (subs translation 10))) #",")]
                    [(parse-float tx) (parse-float ty)])
                  [0 0])
        rects (vec (:content g))]
    (or (apply merge (map (fn [rect]
                            (let [name (keyword (get-in rect [:attrs :id]))
                                  x (get-in rect [:attrs :x])
                                  x (parse-float x)
                                  y (get-in rect [:attrs :y])
                                  y (parse-float y)
                                  w (get-in rect [:attrs :width])
                                  w (parse-float w)
                                  h (get-in rect [:attrs :height])
                                  h (parse-float h)
                                  style (get-in rect [:attrs :style])
                                  fill-index (.indexOf style "fill:")
                                  color (parse-hex-color
                                          (subs style (+ fill-index 6)
                                                (+ fill-index 12)))]
                              {name {:x (/ (+ x tx (/ w 2)) dx)
                                     :y (/ (+ y ty (/ h 2)) dy)
                                     :w (/ w dx)
                                     :h (/ h dy)
                                     :color color}}))
                          rects))
        {})))

(defn get-absolute-region [region box]
  (let [bx (:x box)
        by (:y box)
        bw (:w box)
        bh (:h box)
        {:keys [x y w h]} region]
    (-> region
        (assoc-in [:x] (+ (* x bw) bx (- (/ bw 2))))
        (assoc-in [:y] (+ (* y bh) by (- (/ bh 2))))
        (assoc-in [:w] (* w bw))
        (assoc-in [:h] (* h bh)))))

(defn get-region-at [picture x y]
  (first (find-if (fn [[name region]]
                    (inside-box? (get-absolute-region
                                   region picture) x y))
                  (:regions picture))))

(defn create-png [base x y w h]
  (let [svg-filename (str "res/" base ".svg")
        document (read-xml svg-filename)
        image (if (= w -1)
                (parse-svg-from-map-with-height document h)
                (parse-svg-from-map-with-width document w))
        picture {:x x
                 :y y
                 :w (get-image-width image)
                 :h (get-image-height image)
                 :image image}
        regions (get-svg-regions document)
        png-filename (str "res/" base ".png")
        reg-filename (str "res/" base ".reg")]
    (spit reg-filename regions)
    (write-image! image "png" png-filename)
    (assoc-in picture [:regions] regions)))

(defn create-picture [base x y w h]
  (let [png-filename (str "res/" base ".png")
        reg-filename (str "res/" base ".reg")]
    (if (file-exists? png-filename)
      (let [regions (read-string (slurp reg-filename))
            image (read-image png-filename)]
        {:x x
         :y y
         :w (get-image-width image)
         :h (get-image-height image)
         :image image
         :regions regions})
      (create-png base x y w h))))
