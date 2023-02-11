(import java.awt.Robot)
(import java.awt.event.InputEvent)

(defn get-button-mask [name]
  (case name
    :right InputEvent/BUTTON3_DOWN_MASK
    :left InputEvent/BUTTON1_DOWN_MASK
    :middle InputEvent/BUTTON2_DOWN_MASK))

(defn make-robot [] (new Robot))
(defn robot-mouse-press-robot [robot button] (.mousePress robot button))
(defn robot-mouse-release-robot [robot button] (.mouseRelease robot button))
