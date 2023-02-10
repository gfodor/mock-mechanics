(ns mockmechanics.core
  (:require [mockmechanics.library.util :refer :all]
            [mockmechanics.library.matrix :as matrix]
            [mockmechanics.library.vector :as vector])
  (:import org.lwjgl.glfw.GLFW
           org.lwjgl.system.MemoryUtil
           org.lwjgl.opengl.GL
           org.lwjgl.opengl.GL11
           org.lwjgl.opengl.GL12
           org.lwjgl.opengl.GL13
           org.lwjgl.opengl.GL20
           org.lwjgl.opengl.GL30
           org.lwjgl.glfw.GLFWCursorPosCallback
           org.lwjgl.glfw.GLFWMouseButtonCallback
           org.lwjgl.glfw.GLFWKeyCallback
           org.lwjgl.glfw.GLFWScrollCallback
           org.lwjgl.glfw.GLFWWindowSizeCallback
           org.lwjgl.glfw.GLFWWindowFocusCallback
           org.lwjgl.glfw.GLFWWindowMaximizeCallback
           java.awt.image.BufferedImage
           javax.imageio.ImageIO
           java.io.File
           java.awt.Color
           java.awt.geom.Ellipse2D$Double
           java.awt.RenderingHints
           java.awt.Font
           java.awt.Polygon
           java.awt.geom.AffineTransform
           java.awt.AlphaComposite
           com.bulletphysics.linearmath.Transform
           java.net.ServerSocket
           java.net.Socket
           java.io.BufferedReader
           java.io.InputStreamReader
           java.nio.ByteBuffer
           java.nio.ByteOrder
           org.lwjgl.opengl.GL11
           org.lwjgl.opengl.GL13
           org.lwjgl.opengl.GL20
           java.nio.FloatBuffer
           java.nio.IntBuffer
           java.nio.ByteBuffer
           java.nio.ByteOrder))

(defn gl-println [& forms]
  (println forms)
  (try
    (binding [*out* @out]
      (apply clojure.core/println forms))
    (catch Exception e (println e)))
  )

(defn init-graphics! []
  (GL/createCapabilities)
  (GL11/glClearColor 0 0.5 0.8 0)
  (GL11/glEnable GL11/GL_CULL_FACE)
  (GL11/glCullFace GL11/GL_BACK))

(defn clear-viewport! []
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT
                        GL11/GL_DEPTH_BUFFER_BIT)))

(defn clear-depth-buffer! []
  (GL11/glClear GL11/GL_DEPTH_BUFFER_BIT))

(defn should-close? [window]
  (GLFW/glfwWindowShouldClose window))

(defn poll-events []
  (GLFW/glfwPollEvents))

(defn swap-buffers [window]
  (GLFW/glfwSwapBuffers window))

(defn set-viewport [width height]
  (GL11/glViewport 0 0 width height))

(defn create-key-handler! [window]
  (let [key-handler (proxy [GLFWKeyCallback] []
                      (invoke [window key scancode action mods]
                        (cond
                          (= action GLFW/GLFW_PRESS)
                          (try
                            (swap! world (fn [w] (key-pressed w {:code key})))
                            (reset! time-since-update 0)
                            (catch Exception e))

                          (= action GLFW/GLFW_RELEASE)
                          (try
                            (swap! world (fn [w] (key-released w {:code key})))
                            (reset! time-since-update 0)
                            (catch Exception e)))))]

    (GLFW/glfwSetKeyCallback window key-handler)
    key-handler))

(defn create-mouse-handler! [window]
  (let [mouse-handler (proxy [GLFWMouseButtonCallback] []
                        (invoke [window button action mods]
                          (let [event {:x @mouse-x
                                       :y @mouse-y
                                       :button (get-button-name button)}]
                            (cond
                              (= action GLFW/GLFW_PRESS)
                              (try
                                (reset! mouse-button (get-button-name button))
                                (swap! world (fn [w] (mouse-pressed w event)))
                                (reset! time-since-update 0)
                                (catch Exception e))

                              (= action GLFW/GLFW_RELEASE)
                              (try
                                (swap! world (fn [w] (mouse-released w event)))
                                (reset! mouse-button nil)
                                (reset! time-since-update 0)
                                (catch Exception e))))))]

    (GLFW/glfwSetMouseButtonCallback window mouse-handler)
    mouse-handler))

(defn create-mouse-motion-handler! [window]
  (let [mouse-motion-handler (proxy [GLFWCursorPosCallback] []
                               (invoke [window x y]
                                 (reset! mouse-x x)
                                 (reset! mouse-y y)
                                 (try
                                   (swap! world
                                          (fn [w]
                                            (mouse-moved w {:x x :y y
                                                            :button @mouse-button})))
                                   (when (or @mouse-button
                                             (and (= (:mode @world) :add)
                                                  (= (:add-type @world) :track)))
                                     (reset! time-since-update 0))
                                   (catch Exception e))))]
    (GLFW/glfwSetCursorPosCallback window mouse-motion-handler)
    mouse-motion-handler))

(defn create-mouse-scroll-handler! [window]
  (let [mouse-scroll-handler (proxy [GLFWScrollCallback] []
                               (invoke [window x y]
                                 (try
                                   (swap! world
                                          (fn [w]
                                            (mouse-scrolled w {:x @mouse-x
                                                               :y @mouse-y
                                                               :amount y})))
                                   (reset! time-since-update 0)
                                   (catch Exception e))))]

    (GLFW/glfwSetScrollCallback window mouse-scroll-handler)
    mouse-scroll-handler))

(defn create-window-size-handler! [window]
  (let [handler (proxy [GLFWWindowSizeCallback] []
                  (invoke [window width height]
                    (try
                      (swap! world
                             (fn [w]
                               (if (empty? w)
                                 w
                                 (window-changed w {:width width
                                                    :height height}))))
                      (reset! time-since-update 0)
                      (catch Exception e))))]
    (GLFW/glfwSetWindowSizeCallback window handler)
    handler))

(defn create-window-focus-handler! [window]
  (let [handler (proxy [GLFWWindowFocusCallback] []
                  (invoke [window focused]
                    (try
                      (swap! world
                             (fn [w]
                               (window-focused w focused)))
                      (catch Exception e))))]
    (GLFW/glfwSetWindowFocusCallback window handler)
    handler))

(defn set-title! [text]
  (GLFW/glfwSetWindowTitle @the-window text))

(defn set-window-size! [window width height]
  (GLFW/glfwSetWindowSize window width height))

(defn window-init-and-run! [loop!] 
  (GLFW/glfwInit)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)
  (GLFW/glfwWindowHint GLFW/GLFW_SAMPLES 8)
  (GLFW/glfwWindowHint GLFW/GLFW_MAXIMIZED GLFW/GLFW_FALSE)

  (let [width @window-width
        height @window-height
        window (GLFW/glfwCreateWindow width height "-"
                                      MemoryUtil/NULL MemoryUtil/NULL)]

    (reset! the-window window)
    (create-key-handler! window)
    (create-mouse-handler! window)
    (create-mouse-motion-handler! window)
    (create-mouse-scroll-handler! window)
    (create-window-size-handler! window)
    (create-window-focus-handler! window)

    (GLFW/glfwMakeContextCurrent window)
    (GLFW/glfwSwapInterval 1)
    (GLFW/glfwShowWindow window)

    (GL/createCapabilities)

    (GL11/glViewport 0 0 width height)
    (GL11/glClearColor 0.0 0.0 0.0 0.0)

    (GL11/glEnable GL11/GL_BLEND)
    (GL11/glBlendFunc GL11/GL_SRC_ALPHA GL11/GL_ONE_MINUS_SRC_ALPHA)

    (GL11/glEnable GL11/GL_DEPTH_TEST)

    (loop! window)

    (GLFW/glfwDestroyWindow window)
    (GLFW/glfwTerminate)))

(defn check-shader [shader]
  (let [status (GL20/glGetShaderi shader GL20/GL_COMPILE_STATUS)]
    (when (= status 0)
      (gl-println (GL20/glGetShaderInfoLog shader))
      (GL20/glDeleteShader shader))))

(defn check-program [program]
  (let [status (GL20/glGetProgrami program GL20/GL_LINK_STATUS)]
    (when (= status 0)
      (gl-println (GL20/glGetProgramInfoLog program))
      (GL20/glDeleteProgram program))))

(defn load-shader [filename type]
  (let [shader (GL20/glCreateShader (if (= type :fragment)
                                      GL20/GL_FRAGMENT_SHADER
                                      GL20/GL_VERTEX_SHADER))
        source (slurp filename)]
    (GL20/glShaderSource shader source)
    (GL20/glCompileShader shader)
    (check-shader shader)
    shader))

(defn compile-program [vertex-filename fragment-filename]
  (let [vertex-shader (load-shader vertex-filename :vertex)
        fragment-shader (load-shader fragment-filename :fragment)
        program (GL20/glCreateProgram)]

    (GL20/glAttachShader program vertex-shader)
    (GL20/glAttachShader program fragment-shader)
    (GL20/glLinkProgram program)
    (check-program program)
    program))

(defn make-int-buffer [size]
  (let [bb (ByteBuffer/allocateDirect size)]
    (.order bb (ByteOrder/nativeOrder))
    (.asIntBuffer bb)))

(defn get-attribute-names [index]
  (let [num-active (GL20/glGetProgrami index GL20/GL_ACTIVE_ATTRIBUTES)]
    (map (fn [i]
           (let [size (make-int-buffer 100)
                 type (make-int-buffer 100)
                 name (GL20/glGetActiveAttrib index i size type)
                 index (.indexOf name "[")]
             (if (pos? index)
               (subs name 0 index)
               name)))
         (range num-active))))

(defn get-uniform-names [index]
  (let [num-active (GL20/glGetProgrami index GL20/GL_ACTIVE_UNIFORMS)]
    (map (fn [i]
           (let [size (make-int-buffer 100)
                 type (make-int-buffer 100)
                 name (GL20/glGetActiveUniform index i size type)
                 index (.indexOf name "[")]
             (if (pos? index)
               (subs name 0 index)
               name)))
         (range num-active))))

(defn create-program [base-name]
  (let [v-name (str "res/" base-name "-vert.glsl")
        f-name (str "res/" base-name "-frag.glsl")
        index (compile-program v-name f-name)
        attribute-names (get-attribute-names index)
        uniform-names (get-uniform-names index)
        attributes (apply merge
                          (map (fn [name]
                                 {(location-name->keyword name)
                                  (GL20/glGetAttribLocation index name)})
                               attribute-names))
        uniforms (apply merge
                        (map (fn [name]
                               {(location-name->keyword name)
                                (GL20/glGetUniformLocation index name)})
                             uniform-names))]
    {:index index
     :uniforms uniforms
     :attributes attributes}))

(defn new-image [width height]
  (new BufferedImage
       width
       height
       (. BufferedImage TYPE_INT_ARGB)))

(defn open-image [filename]
  (ImageIO/read (new File filename)))

(defn get-image-width [image]
  (.getWidth image))

(defn get-image-height [image]
  (.getHeight image))

(defn get-image-graphics [image]
  (let [g (.getGraphics image)]
    (.setRenderingHint g
                       RenderingHints/KEY_ANTIALIASING
                       RenderingHints/VALUE_ANTIALIAS_ON)
    g))

(defn clear
  ([image]
   (let [g (get-image-graphics image)
         w (get-image-width image)
         h (get-image-height image)]
     (.setComposite g (AlphaComposite/getInstance AlphaComposite/CLEAR))
     (.fillRect g 0 0 w h)
     (.setComposite g (AlphaComposite/getInstance AlphaComposite/SRC_OVER))))
  ([image color]
   (let [g (get-image-graphics image)
         w (get-image-width image)
         h (get-image-height image)]
     (.setColor g (get-color color))
     (.fillRect g 0 0 w h))))

(defn draw-pixel [image color x y]
  (when (and
          (<= 0 x (dec (get-image-width image)))
          (<= 0 y (dec (get-image-height image))))
    (.setRGB image (int x) (int y) (.getRGB (get-color color))))
  image)

(defn fill-rect [image color x y w h]
  (let [g (get-image-graphics image)
        hw (/ w 2)
        hh (/ h 2)]
    (.setColor g (get-color color))
    (.fillRect g (- x hw) (- y hh) w h)))

(defn draw-rect [image color x y w h]
  (let [g (get-image-graphics image)
        hw (/ w 2)
        hh (/ h 2)]
    (.setColor g (get-color color))
    (.drawRect g (- x hw) (- y hh) w h)))

(defn fill-circle [image color x y r]
  (let [g (get-image-graphics image)]
    (.setColor g (get-color color))
    (.fill g (new Ellipse2D$Double (- x r) (- y r) (* 2 r) (* 2 r)))))

(defn draw-circle [image color x y r]
  (let [g (get-image-graphics image)]
    (.setColor g (get-color color))
    (.draw g (new Ellipse2D$Double (- x r) (- y r) (* 2 r) (* 2 r)))))

(defn get-text-width! [text size]
  (let [image (get-in @world [:ortho-mesh :image])
        g (get-image-graphics image)
        font (new Font "Dialog" Font/PLAIN size)]
    (.stringWidth (.getFontMetrics g font) text)))

(defn draw-text [image color text x y size]
  (let [g (get-image-graphics image)]
    (.setFont g (new Font "Dialog" Font/PLAIN size))
    (.setColor g (get-color color))
    (.drawString g text (int x) (int y))))

(defn draw-text-mono [image color text x y size]
  (let [g (get-image-graphics image)]
    (.setFont g (new Font "monospaced" Font/PLAIN size))
    (.setColor g (get-color color))
    (.drawString g text (int x) (int y))))

(defn draw-ellipse [image color rect]
  (let [g (get-image-graphics image)]
    (.setColor g (get-color color))
    (let [w (* (:w rect) 1.0)
          h (* (:h rect) 1.0)
          ellipse (new Ellipse2D$Double
                       (- (/ w 2))
                       (- (/ h 2))
                       w h)
          angle (to-radians (:angle rect))
          ellipse (.createTransformedShape
                    (AffineTransform/getRotateInstance angle)
                    ellipse)
          ellipse (.createTransformedShape
                    (AffineTransform/getTranslateInstance (:x rect) (:y rect))
                    ellipse)]
      (.draw g ellipse)
      (fill-circle image color (:x rect) (:y rect) 2))))

(defn draw-line [image color x1 y1 x2 y2]
  (let [g (get-image-graphics image)]
    (.setColor g (get-color color))
    (.drawLine g x1 y1 x2 y2)))

(defn fill-polygon [image color points]
  (let [g (get-image-graphics image)]
    (.setColor g (get-color color))
    (let [polygon (new Polygon)]
      (doseq [[x y] points]
        (.addPoint polygon x y))
      (.fillPolygon g polygon))))

(defn draw-polygon [image color points]
  (let [g (get-image-graphics image)]
    (.setColor g (get-color color))
    (let [polygon (new Polygon)]
      (doseq [[x y] points]
        (.addPoint polygon x y))
      (.drawPolygon g polygon))))

(defn draw-image [image image2 x y & corner]
  (let [g (get-image-graphics image)
        w (get-image-width image2)
        h (get-image-height image2)
        x (if (first corner) x (- x (/ w 2)))
        y (if (first corner) y (- y (/ h 2)))]
    (.drawImage g image2 (int x) (int y) nil)))

(defn image->buffer [image]
  (let [w (.getWidth image)
        h (.getHeight image)
        pixels (int-array (* w h))
        bb (ByteBuffer/allocateDirect (* w h 4))]
    (.getRGB image 0 0 w h pixels 0 w)
    (let [ib (.asIntBuffer bb)]
      (.put ib pixels)
      bb)))

(defn reset-texture [mesh]
  (let [id (:texture-id mesh)
        image (:image mesh)
        width (get-image-width image)
        height (get-image-height image)
        buffer (image->buffer image)]
    (GL13/glActiveTexture GL13/GL_TEXTURE0)
    (GL11/glBindTexture GL11/GL_TEXTURE_2D id)
    (GL11/glTexSubImage2D GL11/GL_TEXTURE_2D 0 0 0 width height
                          GL11/GL_RGBA GL11/GL_UNSIGNED_BYTE buffer)
    mesh))

(defn set-texture [mesh]
  (let [id (:texture-id mesh)
        image (:image mesh)
        width (get-image-width image)
        height (get-image-height image)
        buffer (image->buffer image)]
    (GL13/glActiveTexture GL13/GL_TEXTURE0)

    (GL11/glBindTexture GL11/GL_TEXTURE_2D id)
    (GL11/glPixelStorei GL11/GL_UNPACK_ALIGNMENT 1)
    (GL11/glTexImage2D GL11/GL_TEXTURE_2D 0 GL11/GL_RGBA width height 0
                       GL11/GL_RGBA GL11/GL_UNSIGNED_BYTE buffer)
    (GL30/glGenerateMipmap GL11/GL_TEXTURE_2D)

    (GL11/glTexParameteri GL11/GL_TEXTURE_2D
                          GL11/GL_TEXTURE_WRAP_S GL12/GL_CLAMP_TO_EDGE)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D
                          GL11/GL_TEXTURE_WRAP_T GL12/GL_CLAMP_TO_EDGE)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_MIN_FILTER
                          GL11/GL_LINEAR)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_MAG_FILTER
                          GL11/GL_LINEAR)
    mesh))

(defn get-float-buffer [seq]
  (let [array (if (vector? seq)
                (float-array seq)
                seq)
        bb (ByteBuffer/allocateDirect (* (count array) 4))]
    (.order bb (ByteOrder/nativeOrder))
    (let [fb (.asFloatBuffer bb)]
      (.put fb array)
      (.position fb 0)
      fb)))

(defn draw-lighted-mesh! [world mesh transform]
  (let [num-vertices (/ (.capacity (:vertices-buffer mesh)) 3)
        program (get-in world [:programs (:program mesh)])
        program-index (:index program)
        attributes (:attributes program)
        uniforms (:uniforms program)
        model-matrix (matrix/multiply
                       (apply matrix/get-scale (:scale mesh))
                       (get-transform-matrix transform))
        view-matrix (:view-matrix world)
        projection-matrix (:projection-matrix world)
        mv-matrix (matrix/multiply model-matrix view-matrix)
        mvp-matrix (matrix/multiply mv-matrix projection-matrix)
        itmv-matrix (matrix/get-transpose (matrix/get-inverse mv-matrix))]

    (GL20/glUseProgram program-index)
    (GL20/glUniformMatrix4fv (:itmv-matrix uniforms) false
                             (get-float-buffer itmv-matrix))

    (GL20/glUniformMatrix4fv (:mvp-matrix uniforms) false
                             (get-float-buffer mvp-matrix))

    (GL20/glVertexAttribPointer (:position attributes) 3 GL11/GL_FLOAT
                                false 0 (:vertices-buffer mesh))

    (GL20/glEnableVertexAttribArray (:position attributes))

    (GL20/glVertexAttribPointer (:normal attributes) 3 GL11/GL_FLOAT
                                false 0 (:normals-buffer mesh))
    (GL20/glEnableVertexAttribArray (:normal attributes))

    (if-let [[r g b a] (:color mesh)]
      (GL20/glUniform4f (:material-color uniforms) r g b a)
      (do
        (GL20/glVertexAttribPointer (:texture-coordinates attributes) 2 GL11/GL_FLOAT
                                    false 0 (:texture-coordinates-buffer mesh))
        (GL20/glEnableVertexAttribArray (:texture-coordinates attributes))
        (GL13/glActiveTexture GL13/GL_TEXTURE0)
        (GL11/glBindTexture GL11/GL_TEXTURE_2D (:texture-id mesh))
        (GL20/glUniform1i (:texture-diffuse uniforms) 0)))

    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 num-vertices)))

(defn draw-colored-mesh! [world mesh transform]
  (let [num-vertices (/ (.capacity (:vertices-buffer mesh)) 3)
        program (get-in world [:programs (:program mesh)])
        program-index (:index program)
        attributes (:attributes program)
        uniforms (:uniforms program)
        model-matrix (matrix/multiply
                       (apply matrix/get-scale (:scale mesh))
                       (get-transform-matrix transform))
        view-matrix (:view-matrix world)
        projection-matrix (:projection-matrix world)
        mv-matrix (matrix/multiply model-matrix view-matrix)
        mvp-matrix (matrix/multiply mv-matrix projection-matrix)
        itmv-matrix (matrix/get-transpose (matrix/get-inverse mv-matrix))]

    (GL20/glUseProgram program-index)
    (GL20/glUniformMatrix4fv (:itmv-matrix uniforms) false
                             (get-float-buffer itmv-matrix))
    (GL20/glUniformMatrix4fv (:mvp-matrix uniforms) false
                             (get-float-buffer mvp-matrix))

    (GL20/glVertexAttribPointer (:position attributes) 3 GL11/GL_FLOAT
                                false 0 (:vertices-buffer mesh))
    (GL20/glEnableVertexAttribArray (:position attributes))

    (GL20/glVertexAttribPointer (:normal attributes) 3 GL11/GL_FLOAT
                                false 0 (:normals-buffer mesh))
    (GL20/glEnableVertexAttribArray (:normal attributes))

    (GL20/glVertexAttribPointer (:color attributes) 4 GL11/GL_FLOAT
                                false 0 (:colors-buffer mesh))
    (GL20/glEnableVertexAttribArray (:color attributes))

    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 num-vertices)))

(defn draw-textured-mesh! [world mesh transform]
  (let [num-vertices (/ (.capacity (:vertices-buffer mesh)) 3)
        program (get-in world [:programs (:program mesh)])
        program-index (:index program)
        attributes (:attributes program)
        uniforms (:uniforms program)
        model-matrix (matrix/multiply
                       (apply matrix/get-scale (:scale mesh))
                       (get-transform-matrix transform))
        view-matrix (:view-matrix world)
        projection-matrix (:projection-matrix world)
        mv-matrix (matrix/multiply model-matrix view-matrix)
        mvp-matrix (matrix/multiply mv-matrix projection-matrix)
        itmv-matrix (matrix/get-transpose (matrix/get-inverse mv-matrix))]

    (GL20/glUseProgram program-index)
    (GL20/glUniformMatrix4fv (:itmv-matrix uniforms) false
                             (get-float-buffer itmv-matrix))

    (GL20/glUniformMatrix4fv (:mvp-matrix uniforms) false
                             (get-float-buffer mvp-matrix))

    (GL20/glVertexAttribPointer (:position attributes) 3 GL11/GL_FLOAT
                                false 0 (:vertices-buffer mesh))

    (GL20/glEnableVertexAttribArray (:position attributes))

    (GL20/glVertexAttribPointer (:normal attributes) 3 GL11/GL_FLOAT
                                false 0 (:normals-buffer mesh))
    (GL20/glEnableVertexAttribArray (:normal attributes))

    (GL20/glVertexAttribPointer (:texture-coordinates attributes) 2 GL11/GL_FLOAT
                                false 0 (:texture-coordinates-buffer mesh))
    (GL20/glEnableVertexAttribArray (:texture-coordinates attributes))

    (GL13/glActiveTexture GL13/GL_TEXTURE0)
    (GL11/glBindTexture GL11/GL_TEXTURE_2D (:texture-id mesh))
    (GL20/glUniform1i (:texture-diffuse uniforms) 0)

    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 num-vertices)))
