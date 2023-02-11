(ns mockmechanics.core
  (:require [mockmechanics.library.util :refer :all]
            [mockmechanics.library.xml :refer :all]
            [clojure.string :refer [split]])
  (:import org.apache.batik.transcoder.image.ImageTranscoder
           org.apache.batik.transcoder.TranscoderInput
           org.apache.batik.transcoder.TranscoderOutput
           org.apache.batik.transcoder.TranscodingHints
           org.apache.batik.anim.dom.SVGDOMImplementation
           org.apache.batik.util.SVGConstants
           java.io.File
           javax.imageio.ImageIO
           java.io.ByteArrayInputStream
           java.awt.image.BufferedImage))

(defn parse-svg-from-map [document width height]
  (let [transcoder-hints (new TranscodingHints)
        result (atom nil)]
    (.put transcoder-hints ImageTranscoder/KEY_XML_PARSER_VALIDATING Boolean/FALSE)
    (.put transcoder-hints ImageTranscoder/KEY_DOM_IMPLEMENTATION
          (SVGDOMImplementation/getDOMImplementation))
    (.put transcoder-hints ImageTranscoder/KEY_DOCUMENT_ELEMENT_NAMESPACE_URI
          SVGConstants/SVG_NAMESPACE_URI)
    (.put transcoder-hints ImageTranscoder/KEY_DOCUMENT_ELEMENT "svg")
    (.put transcoder-hints ImageTranscoder/KEY_WIDTH (float width))
    (.put transcoder-hints ImageTranscoder/KEY_HEIGHT (float height))

    (let [string (xml->str document)
          bais (new ByteArrayInputStream (.getBytes string))
          input (new TranscoderInput bais)
          t (proxy [ImageTranscoder] []
              (createImage [w h]
                (new BufferedImage w h BufferedImage/TYPE_INT_ARGB))

              (writeImage [image out]
                (reset! result image)))]

      (.setTranscodingHints t transcoder-hints)
      (.transcode t input nil)
      @result)))

(defn write-image! [image type filename]
  (let [file (new File filename)]
    (ImageIO/write image "png" (new File filename))))

(defn read-image [filename]
  (let [file (new File filename)]
    (ImageIO/read file)))
