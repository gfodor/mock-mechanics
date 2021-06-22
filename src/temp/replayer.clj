
(import java.awt.Robot)
(import java.awt.event.InputEvent)

(require '[clojure.java.shell :refer [sh]])
(require '[clojure.set :refer [difference]])

;;----------------------------------------------------------------------;;
;; .obj export/import

(defn export-path [points filename]
  (let [points (read-string (str "(" points ")"))
        final-time (last (last points))
        w @window-width
        h @window-height
        points (concat [[w 0 0] [w h 0] [0 h 0]] points)
        ratio (float (/ w h))]
    (with-open [writer (clojure.java.io/writer filename)]
      (.write writer (format "# final-time %s\n" final-time))
      (.write writer (format "# width %s\n" w))
      (.write writer "o Path\n")
      (doseq [[x y t] points]
        (.write writer (format "v %s %s %s\n"
                               (/ x (float w))
                               (/ t (float final-time))
                               (/ y (float w))
                               )))
      (doseq [i (range 1 (count points))]
        (if (not= i 3)
          (.write writer (format "f %s %s\n" i (inc i))))))))

(defn import-path [filename]
  (let [lines (with-open [rdr (clojure.java.io/reader filename)]
                (vec (line-seq rdr)))
        [_ [_ _ ratio] _ & vertices]
        (map parse-line
             (filter (fn [line]
                       (.startsWith line "v "))
                     lines))

        vertices (sort-by second vertices)
        final-time (>> lines
                       (find-if #(.startsWith % "# final-time") .)
                       (subs .  13)
                       (read-string .))
        width (>> lines
                  (find-if #(.startsWith % "# width") .)
                  (subs .  8)
                  (read-string .))]
    (println!
     (map (fn [[x y z]]
            [(round (* x width))
             (round (* z width))
             (round (* y final-time))])
          vertices))))

;;----------------------------------------------------------------------;;
;; robot

(defn get-window-coordinates []
  (map parse-int
       (clojure.string/split (:out (sh "./window-coords.sh")) #"\n")))

(def robot (atom nil))

(defn robot-move [[x y]]
  (let [{:keys [robot origin]} @robot
        [ox oy] origin]
    (.mouseMove robot (+ ox x) (+ oy y))))

(defn get-button-mask [name]
  (case name
    :right InputEvent/BUTTON3_DOWN_MASK
    :left InputEvent/BUTTON1_DOWN_MASK
    :middle InputEvent/BUTTON2_DOWN_MASK))

(defn robot-set-active! [value]
  (swap! robot
         (fn [r]
           (assoc-in r [:active] value))))

(defn robot-mouse-press [button]
  (.mousePress (:robot @robot) (get-button-mask button)))

(defn robot-mouse-release [button]
  (.mouseRelease (:robot @robot) (get-button-mask button)))

;;----------------------------------------------------------------------;;
;; compile

(defn get-descaled-relative-transform [world part-name change]
  (let [parent-name (get-parent-part world part-name)
        part (get-in world [:parts part-name])
        offset (if (= (:type part) :track)
                 change
                 (vector-multiply change 0.5))
        offset (vector-multiply offset -1)
        offset-transform (make-transform offset [1 0 0 0])
        relative-transform (get-in world [:parts parent-name
                                          :children part-name])]
    (combine-transforms offset-transform relative-transform)))

(defn create-part-instructions [writer world part-name]
  (let [parent-name (get-parent-part world part-name)
        part (get-in world [:parts part-name])
        type (:type part)
        properties (concat
                    (keys (get-in world [:info type :properties]))
                    (filter #(not= % :.) (:properties world))
                    '(:color))
        color (get-in world [:info type :color])
        new-part (create-part type color 0 (:info world))
        scale-change (vector-subtract (:scale part) (:scale new-part))]

    (let [relative-transform (get-descaled-relative-transform
                              world part-name scale-change)
          position (get-transform-position relative-transform)
          rotation (get-transform-rotation relative-transform)]
      (.write writer (format "add %s to %s at %s %s\n"
                             (dekeyword part-name)
                             (dekeyword parent-name)
                             position
                             rotation)))
    (doseq [i (range 3)]
      (if (not (float= (nth scale-change i) 0.0))
        (.write writer (format "scale %s by %s\n"
                               (dekeyword part-name)
                               (assoc [0 0 0] i (nth scale-change i))))))
    
    (doseq [property properties]
      (if (not (= (get part property) (get new-part property)))
        (let [value (if (= property :color)
                      (reverse-get-color (get part property))
                      (get part property))
              value (if (keyword value)
                      (dekeyword value)
                      value)
              value (if (= type :wagon)
                      (* value (reduce + (:track-lengths part)))
                      value)]
          (.write writer (format "set %s of %s to %s\n"
                                 (dekeyword property)
                                 (dekeyword part-name)
                                 value)))))
    (if (and (= type :chip)
             (not (empty? (:functions part))))
      (.write writer (format "set-functions of %s to %s\n"
                             (dekeyword part-name)
                             (:functions part))))

    (if (and (= type :motherboard)
             (not (empty? (:pins part))))
      (.write writer (format "set-graph of %s to %s\n"
                       (dekeyword part-name)
                       (select-keys part [:pins :gates :connections]))))
    ))

(defn get-part-number [part-name]
  (parse-int (second (re-find #":[a-z]*([0-9]*)" (str part-name)))))

(defn build-parents-map [mp parts part-name]
  (reduce (fn [m child-name]
            (-> m
              (assoc-in [child-name] part-name)
              (build-parents-map parts child-name)))
    mp
    (keys (:children (get-in parts [part-name])))))

(defn ancestor? [mp parent child]
  (->> child
    (iterate #(get mp %))
    (take-while not-nil?)
    (get-index parent)
    (boolean)))

(defn create-instructions [world filename]
  (let [sorted-names (>> (:parts world)
                         (keys .)
                         (into #{} .)
                         (clojure.set/difference . #{:ground-part})
                         (vec .)
                         (sort-by get-part-number .))
        parent-map (build-parents-map {} (:parts world) :ground-part)
        sorted-names (sort-by identity (partial ancestor? parent-map)
                       sorted-names)
        world (reduce (fn [w part-name]
                        (set-value-0-transform w part-name))
                      world
                      sorted-names)
        filename (str "res/" filename ".txt")]
    (with-open [writer (clojure.java.io/writer filename)]
      (doseq [part-name sorted-names]
        (create-part-instructions writer world part-name)))
    world))


;; ;;----------------------------------------------------------------------;;
;; ;; mouse events

;; (defn insert-instruction! [filename index instruction]
;;   (let [lines (with-open [rdr (clojure.java.io/reader filename)]
;;                 (vec (line-seq rdr)))
;;         new-lines (vector-insert lines instruction index)]
;;     (spit filename (apply str (interpose "\n" new-lines)))))

;; (defn change-event [event start-time]
;;   [(int (:x event)) (int (:y event)) (- (get-current-time) start-time)])

;; (defn replay-pressed [world event]
;;   (if (:replay-filename world)
;;     ;; (if (:active @robot)
;;     ;;   world
;;     ;;   (-> world
;;     ;;       (assoc-in [:replay-button] (dekeyword (:button event)))
;;     ;;       (assoc-in [:replay-events]
;;     ;;                 [(change-event event (:press-time world))])))
;;     (do
;;       ;; (println! "pressed" event)
;;       world)
;;     world))

;; (defn replay-moved [world event]
;;   (if (:replay-filename world)
;;     ;; (if (not-nil? (:replay-events world))
;;     ;;   (update-in world [:replay-events]
;;     ;;              #(conj % (change-event event (:press-time world))))
;;     ;;   world)
;;     (do
;;       ;; (println! "moved" event)
;;       world)
;;     world))

;; (defn replay-released [world event]
;;   (if (:replay-filename world)
;;     ;; (if (:active @robot)
;;     ;;   world
;;     ;;   (let [points (conj (:replay-events world)
;;     ;;                      (change-event event (:press-time world)))
;;     ;;         button (:replay-button world)
;;     ;;         instruction (str "mouse " button " " (join " " points))]
;;     ;;     (insert-instruction! (str "res/" (:replay-filename world) ".txt")
;;     ;;                          (:instruction-index world)
;;     ;;                          instruction)
;;     ;;     (-> world
;;     ;;         (dissoc-in [:replay-events])
;;     ;;         (update-in [:instruction-index] inc))))
;;     (do
;;       ;; (println! "released" event)
;;       world)
;;     world))

;;----------------------------------------------------------------------;;
;; run

(defn update-history [world]
  (update-in world [:replay-history]
             #(conj % {:parts (:parts world)
                       :camera (:camera world)})))

(defn get-part-type [part-name]
  (keyword (second (re-find #":([a-z]*)" (str part-name)))))

(defn run-add-instruction [world instruction]
  (let [[_ part-name
         _ parent-name
         _ position rotation] (read-string (str "[" instruction "]"))
        part-name (keyword part-name)
        parent-name (keyword parent-name)
        type (get-part-type part-name)
        layer (apply min (:visible-layers world))
        color (get-in world [:info type :color])
        part (create-part type color layer (:info world))
        transform (make-transform position rotation)
        prepare-wagon (fn [w]
                        (if (= type :wagon)
                          (set-wagon-loop w part-name parent-name)
                          w))]
    (-> world                           
      (assoc-in [:parts part-name] part)
      (assoc-in [:parts parent-name :children part-name] transform)
      (prepare-wagon)
      (compute-transforms :parts)
      (update-history)
      (tree-changed))))

;; (defn run-set-instruction [world instruction]
;;   (let [[_ property-name
;;          _ part-name
;;          _ value] (read-string (str "[" instruction "]"))
;;         property-name (keyword property-name)
;;         part-name (keyword part-name)
;;         value (if (symbol? value) (keyword value) value)]
;;     (-> world
;;         (set-part-value part-name property-name (str value))
;;         (update-history)
;;         (tree-changed))))

(defn scale-animation [world animation]
  (let [{:keys [t start-scale final-scale
                start-transform final-transform
                part-name parent-name]} animation]
    (cond
      (float= t 0.0)
      (tree-will-change world)

      (float= t 1.0)
      (-> world
          (assoc-in [:parts part-name :scale] final-scale)
          (assoc-in [:parts part-name :transform] final-transform)
          (create-relative-transform part-name parent-name)
          (update-history)
          (tree-changed))

      :else
      (let [scale (vector-interpolate start-scale final-scale t)
            transform (interpolate-transforms start-transform
                                              final-transform t)]
        (-> world
            (assoc-in [:parts part-name :scale] scale)
            (assoc-in [:parts part-name :transform] transform))))))

(defn run-scale-instruction [world instruction]
  (let [[_ part-name
         _ change] (read-string (str "[" instruction "]"))
        part-name (keyword part-name)
        parent-name (get-parent-part world part-name)
        part (get-in world [:parts part-name])
        offset (if (= (:type part) :track)
                 change
                 (vector-multiply change 0.5))
        rotation (get-rotation-component (:transform part))
        offset (apply-transform rotation offset)
        offset-transform (make-transform offset [1 0 0 0])
        transform (:transform part)
        final-transform (combine-transforms transform offset-transform)]
    (assoc-in world [:animation]
      {:t 0.0
       :time 0.3
       :fn scale-animation
       :part-name part-name
       :parent-name parent-name
       :start-scale (:scale part)
       :final-scale (vector-add (:scale part) change)
       :start-transform transform
       :final-transform final-transform})))

(defn run-set-color-instruction [world instruction]
  (let [[_ _ _ part-name _ color] (read-string (str "[" instruction "]"))
        color (keyword color)
        part-name (keyword part-name)]
    (-> world
      (assoc-in [:parts part-name :color] color)
      (update-history)
      (tree-changed))))

(defn value-animation [world animation]
  (let [{:keys [t part-name final-value]} animation]
    (cond
      (float= t 0.0)
      (tree-will-change world)

      (float= t 1.0)
      (-> world
        (assoc-in [:parts part-name :value] final-value)
        (update-history)
        (tree-changed))

      :else
      (-> world
        (assoc-in [:parts part-name :value] (* t final-value))
        (redraw)))))

(defn run-set-value-instruction [world instruction]
  (let [[_ _ _ part-name _ value] (read-string (str "[" instruction "]"))
        part-name (keyword part-name)
        part (get-in world [:parts part-name])
        value (if (= (:type part) :wagon)
                (/ value (reduce + (:track-lengths part)))
                value)
        time (if (= (:type part) :wagon)
               (abs value)
               (* 2 (abs value)))]
    (assoc-in world [:animation]
      {:t 0.0
       :time time
       :fn value-animation
       :part-name part-name
       :final-value value})))

;; (defn run-set-functions-instruction [world instruction]
;;   ;; (let [[_ property-name
;;   ;;        _ part-name
;;   ;;        _ value] (read-string (str "[" instruction "]"))
;;   ;;       property-name (keyword property-name)
;;   ;;       part-name (keyword part-name)
;;   ;;       value (if (symbol? value) (keyword value) value)]
;;   ;;   (-> world
;;   ;;       (set-part-value part-name property-name (str value))
;;   ;;       (update-history)
;;   ;;       (tree-changed)))
;;   (println! "set functions")
;;   world
;;   )

;; (defn run-set-graph-instruction [world instruction]
;;   ;; (let [[_ property-name
;;   ;;        _ part-name
;;   ;;        _ value] (read-string (str "[" instruction "]"))
;;   ;;       property-name (keyword property-name)
;;   ;;       part-name (keyword part-name)
;;   ;;       value (if (symbol? value) (keyword value) value)]
;;   ;;   (-> world
;;   ;;       (set-part-value part-name property-name (str value))
;;   ;;       (update-history)
;;   ;;       (tree-changed)))
;;   (println! "set graph")
;;   world
;;   )

;; (defn interpolate-mouse [start end]
;;   (let [ts (last start)
;;         te (last end)
;;         start (take 2 start)
;;         end (take 2 end)
;;         d (distance start end)
;;         v (vector-subtract end start)
;;         interval (- te ts)
;;         dt 20.0
;;         num-steps (int (/ interval dt))
;;         extra-time (int (mod interval dt))]
;;     (robot-move start)
;;     (dotimes [i (dec num-steps)]
;;       (sleep dt)
;;       (robot-move (vector-add start (vector-multiply v (/ i num-steps)))))
;;     (sleep extra-time)
;;     (robot-move end)))

;; (defn run-mouse-instruction [world instruction]
;;   (let [[_ button & points] (read-string (str "(" instruction ")"))
;;         button (keyword button)]

;;     (.start
;;      (new Thread
;;           (proxy [Runnable] []
;;             (run []
;;               (robot-set-active! true)
;;               (robot-move (take 2 (first points)))
;;               (robot-mouse-press button)
;;               (dotimes [i (dec (count points))]
;;                 (interpolate-mouse (nth points i)
;;                                    (nth points (inc i))))
;;               (sleep 16)
;;               (robot-mouse-release button)
;;               (sleep 100) ;;##########################
;;               (robot-set-active! false)
;;               ))))
;;     world))

(defn run-sleep-instruction [world instruction]
  (sleep (parse-int (subs instruction 6)))
  world)

(defn run-set-variable-instruction [world instruction]
  (let [[_ _ key _ value] (read-string (str "(" instruction ")"))
        key (keyword key)
        value (if (symbol value)
                (keyword value)
                value)]
    (-> world
      (assoc-in [key] value)
      (update-history))))

(defn run-instruction [world instruction]
  (let [words (split instruction #" ")
        instruction-name (if (= (first words) "set")
                           (str (first words) "-" (second words))
                           (first words))]
    (if-let [function (-> (str "temp.core/run-"
                               instruction-name
                               "-instruction")
                          (symbol)
                          (resolve))]
      (do
        (println! (subs instruction 0 (min 100 (count instruction))))
        (function world instruction))
      (do
        (println! "invalid instruction")
        world))))

;;----------------------------------------------------------------------;;
;; playback

(defn update-variable [key value result variables]
  (if (= (get-in variables [key]) value)
    [result variables]
    [(conj result (str "set variable " (dekeyword key)
                    " to " (dekeyword value)))
     (assoc-in variables [key] value)]))

(defmacro change-mode-submode [mode submode-name submode]
  (let [r (gensym 'results)
        v (gensym 'variables)]
    `(let [[~r ~v] (update-variable :mode ~mode ~'result ~'variables)
           [~r ~v] (update-variable ~submode-name ~submode ~r ~v)
           ~r (conj ~r ~'instruction)]
       (~'extend-instructions-helper ~r (rest ~'instructions) ~v))))

(defn extend-instructions-helper [result instructions variables]
  (if (empty? instructions)
    result
    (let [instruction (first instructions)
          atoms (split instruction #" ")]
      (cond
        (.startsWith instruction "add")
        (let [type (get-part-type (keyword (second atoms)))]
          (change-mode-submode :add :add-type type))

        (.startsWith instruction "scale")
        (let [subcommand (keyword (first atoms))]
          (change-mode-submode :edit :edit-subcommand subcommand))

        (.startsWith instruction "set color")
        (let [color (keyword (last atoms))]
          (change-mode-submode :color :current-color color))

        (.startsWith instruction "set value")
        (let [selected (keyword (nth atoms 3))]
          (change-mode-submode :property :selected-part selected))

        :else
        (recur (conj result instruction)
          (rest instructions) variables)))))

(defn extend-instructions [instructions]
  (let [variables {:mode :simulation
                   :add-type :block
                   :edit-subcommand :move
                   :current-color :red}]
    (extend-instructions-helper [] instructions variables)))

(def replaying (atom false))

(defn run-instructions! []
  (if @replaying
    (reset! replaying false)
    (let [filename (str "res/" (get-thing! [:replay-filename]) ".txt")
          instructions (with-open [rdr (clojure.java.io/reader filename)]
                         (vec (line-seq rdr)))
          instructions (extend-instructions instructions)
          delay 500]
      (reset! replaying true)
      (while (and @replaying
               (< (:instruction-index @world) (count instructions)))
        (let [instruction (nth instructions (:instruction-index @world))]
          (update-thing! [:instruction-index] inc)
          (when (not (empty? instruction))
            (update-thing! [] #(run-instruction % instruction))
            (while (get-thing! [:animation]))
            (redraw!)
            (sleep delay))))
      (reset! replaying false))))

(defn toggle-run-instructions [world]
  (.start
    (new Thread
      (proxy [Runnable] []
        (run []
          (run-instructions!)))))
  world)

;;----------------------------------------------------------------------;;

(defn replay-draw [world]
  (let [x (- (:window-width world) 10)
        y (- (:window-height world) 10 105)]
    (fill-rect! :black x y 20 20)
    (draw-text! :white "R" (- x 4) (+ y 5) 15)))

(defn toggle-replay [world]
  (if (:replay-filename world)
    (dissoc-in world [:replay-filename])
    (do
      (when (nil? @robot)
        (reset! robot {:robot (new Robot)
                       :origin (get-window-coordinates)
                       :active false}))
      (read-input world
        (fn [w text]
          (-> w
            (assoc-in [:replay-filename] text)
            (assoc-in [:instruction-index] 0)
            (assoc-in [:replay-history] [])
            (update-history)))))))

;; (defn remove-mark [instruction]
;;   (if (.startsWith instruction "*")
;;     (subs instruction 2)
;;     instruction))

(defn replayer-restart [world]
  (let [new-history (vec (take 1 (:replay-history world)))]
    (-> world
      (assoc-in [:mode] :simulation)
      (assoc-in [:add-type] :block)
      (assoc-in [:edit-subcommand] :move)
      (assoc-in [:selected-part] nil)
      (assoc-in [:current-color] :red)
      (assoc-in [:parts] (:parts (last new-history)))
      (assoc-in [:camera] (:camera (last new-history)))
      (compute-camera)
      (assoc-in [:replay-history] new-history)
      (assoc-in [:instruction-index] 0)
      (tree-changed))))

(defn replay-forward [world]
  (if (and
        (:replay-filename world)
        (nil? (:animation world)))
    (let [filename (str "res/" (:replay-filename world) ".txt")
          instructions (with-open [rdr (clojure.java.io/reader filename)]
                         (vec (line-seq rdr)))
          instructions (extend-instructions instructions)
          index (:instruction-index world)]
      (if (< index (count instructions))
        (-> world
          (run-instruction (nth instructions index))
          (update-in [:instruction-index] inc))
        world))
    world))

(defn replay-back [world]
  (if (and
        (:replay-filename world)
        (> (:instruction-index world) 0))
    (let [new-history (pop (:replay-history world))
          filename (str "res/" (:replay-filename world) ".txt")
          instructions (with-open [rdr (clojure.java.io/reader filename)]
                         (vec (line-seq rdr)))
          instructions (extend-instructions instructions)
          instruction (nth instructions (dec (:instruction-index world)))]       
      (println! "<<" (subs instruction 0 (min 100 (count instruction))))
      (-> world
          (assoc-in [:parts] (:parts (last new-history)))
          (assoc-in [:camera] (:camera (last new-history)))
          (compute-camera)
          (assoc-in [:replay-history] new-history)
          (update-in [:instruction-index] dec)
          (tree-changed)))
    world))

;;----------------------------------------------------------------------;;
