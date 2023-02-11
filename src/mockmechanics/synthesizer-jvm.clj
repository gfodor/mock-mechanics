(import '(javax.sound.midi MidiSystem Synthesizer))

(def synth (doto (MidiSystem/getSynthesizer) .open))

(defn note-on [note]
  (let [channel (aget (.getChannels synth) 0)]
    (.noteOn channel note 127)))

(defn note-off [note]
  (let [channel (aget (.getChannels synth) 0)]
    (.noteOff channel note 127)))
