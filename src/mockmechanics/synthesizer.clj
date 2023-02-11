(load "synthesizer-js")

(defn get-frequency [note]
  (* 8.175805469120409 (Math/pow 1.059463 note)))

(defn get-note [frequency]
  (int (Math/round (/ (Math/log (/ frequency 8.1758))
                      (Math/log 1.059463)))))
