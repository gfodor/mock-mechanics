(defn reader-for-filename [filename]
  (clojure.java.io/reader filename))

(defn parse-int [s]
  (Integer/parseInt s))

(defn parse-float [string]
  (try
    (Float/parseFloat string)
    (catch Exception e nil)))

(defn run-in-thread! [f]
  (let [thread (Thread. f)]
    (.start thread)
    thread))
