(ns mockmechanics.core
  (:require [mockmechanics.library.matrix :as matrix]
            [clojure.string :refer [join]]))

(defn draw-animated-mesh! [world mesh transform]
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

    (GL20/glVertexAttribPointer (:weights attributes) 4 GL11/GL_FLOAT
                                false 0 (:weights-buffer mesh))
    (GL20/glEnableVertexAttribArray (:weights attributes))

    (GL20/glVertexAttribPointer (:bone-indices attributes) 4 GL11/GL_FLOAT
                                false 0 (:bone-indices-buffer mesh))
    (GL20/glEnableVertexAttribArray (:bone-indices attributes))

    (GL20/glUniformMatrix4fv (:bone-matrices uniforms)
                             false
                             (get-float-buffer
                               (nth (:bone-matrices mesh)
                                    (round (:index mesh)))))

    (GL20/glUniformMatrix4fv (:inverse-bind-pose-matrices uniforms)
                             false
                             (get-float-buffer
                               (:inverse-bind-pose-matrices mesh)))

    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 num-vertices)))
