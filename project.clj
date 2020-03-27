(defproject temp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojars.nakkaya/vecmath "1"]
                 [org.clojars.nakkaya/jbullet "20101010"]
                 [matrix/matrix "1.0.0"]
                 [batik/batik "1.7.1"]
                 ]
  :plugins [[cider/cider-nrepl "0.21.1"]]
  :main ^:skip-aot temp.core
  :target-path "target/%s"

  :resource-paths ["libs/lwjgl.jar"
                   "libs/lwjgl-natives-linux.jar"
                   
                   "libs/lwjgl-glfw.jar"
                   "libs/lwjgl-glfw-natives-linux.jar"

                   "libs/lwjgl-jemalloc.jar"
                   "libs/lwjgl-jemalloc-natives-linux.jar"
                   
                   "libs/lwjgl-opengl.jar"
                   "libs/lwjgl-opengl-natives-linux.jar"]
  
  :profiles {:uberjar {:aot :all}})
