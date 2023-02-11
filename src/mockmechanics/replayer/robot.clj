(def robot (atom nil))

(load "replayer/robot-js")

(defn reset-robot! []
  (reset! robot {:robot (make-robot)
                 :origin (get-window-coordinates)
                 :active false}))

(defn robot-move [[x y]]
  (let [{:keys [robot origin]} @robot
        [ox oy] origin]
    (.mouseMove robot (+ ox x) (+ oy y))))

(defn robot-set-active! [value]
  (swap! robot
         (fn [r]
           (-> r
               (assoc-in [:active] value)
               (assoc-in [:origin] (get-window-coordinates))))))

(defn robot-mouse-press [button] (robot-mouse-press-robot (:robot @robot) button))
(defn robot-mouse-release [button] (robot-mouse-release-robot (:robot @robot) button))
