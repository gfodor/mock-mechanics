
[collision-probe button lamp down-chip
 up-chip new-part-chip copy-button]

(defn collision? []
  (let [blocks ...
        points ...
        collision-parts (map #(get-part [% [0 0 1]]) points)]
    (any? #(not (in? % blocks)) collision-parts)))

(fn [part-name]
  (set-value lamp (get-value button))

  (when (= part-name button)
    (while (on? button)
      (activate down-chip)

      (when (on? collision-probe)
        (press-button copy-button) (sleep 200) ;;##########################b
        (activate up-chip)
        (dotimes [i (rand-int 5)]
          (activate new-part-chip))
        ))))
