{:ground-children {:ground-part {:position [0.0 0.0 0.0], :rotation [-1.0 0.0 0.0 90.00000250447816]}}, :parts {:block8668 {:children {:probe8682 {:position [0.7 -0.100000024 0.0], :rotation [1.0 -4.371139E-8 0.0 180.00000500895632]}}, :color [255 255 0], :scale (1.5 0.1 0.5), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :cpu12880 {:children nil, :color :blue, :pins {:button8684 {:x 80, :trigger true, :value 0}, :chip8669 {:x 300, :trigger false, :value 0}, :chip8670 {:x 420, :trigger false, :value 0}, :probe8682 {:x 180, :trigger false, :value 0}}, :scale [0.3 0.1 0.3], :gates {:gate-not12887 {:type :not, :x 180, :y 520, :tab 0}, :gate-and12889 {:type :and, :x 180, :y 560, :tab 0}, :gate-and15203 {:type :and, :x 180, :y 560, :tab 1}}, :value 0, :type :cpu, :connections {:connection12888 {:points [:probe8682 :gate-not12887], :tab 0}, :connection12890 {:points [:button8684 [80 560] :gate-and12889], :tab 0}, :connection12892 {:points [:gate-not12887 :gate-and12889], :tab 0}, :connection12893 {:points [:gate-and12889 [300 560] :chip8669], :tab 0}, :connection15204 {:points [:button8684 [80 560] :gate-and15203], :tab 1}, :connection15205 {:points [:probe8682 :gate-and15203], :tab 1}, :connection15206 {:points [:gate-and15203 [420 560] :chip8670], :tab 1}}, :tab 1}, :block8671 {:children {:probe8683 {:position [0.3 0.5 0.0], :rotation [0.0 0.0 -1.0 90.00000250447816]}, :cpu12880 {:position [0.0 -0.6 0.3], :rotation [1.0 0.0 0.0 90.00000250447816]}}, :color :white, :scale (0.5 1.7000000000000002 0.5), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :probe8683 {:children nil, :color :purple, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :probe, :inputs nil, :outputs nil}, :probe8682 {:children nil, :color :purple, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :probe, :inputs nil, :outputs nil}, :ground-part {:children {:block8671 {:position [-0.75 -0.25 0.85], :rotation [1.0 0.0 0.0 90.00000250447816]}, :block8666 {:position [0.75 -0.25 0.25], :rotation [1.0 0.0 0.0 90.00000250447816]}, :button8684 {:position [0.75 -1.25 0.1], :rotation [1.0 0.0 0.0 90.00000250447816]}}, :color [40 40 40], :scale [12 12 1], :value 0, :functions nil, :type :ground, :inputs nil, :outputs nil}, :track8667 {:children {:block8668 {:position [0.49999994 0.049999952 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :color :red, :scale [0.1 1 0.1], :value 1.0, :functions nil, :type :track, :inputs nil, :outputs nil}, :chip8669 {:children nil, :color :gray, :scale [0.3 0.1 0.3], :value 0, :time 1.0, :functions {:track8667 {:points [[0 0] [1.0 0.5]], :relative false, :final-points [[0 0] [1.0 0.5]]}}, :type :chip, :inputs nil, :outputs nil, :view {:offset [0 0], :zoom 1}, :final-time 1.0}, :button8684 {:children nil, :color [255 0 0], :scale [0.5 0.2 0.5], :value 0, :functions nil, :type :button, :inputs nil, :outputs nil}, :chip8670 {:children nil, :color :gray, :scale [0.3 0.1 0.3], :value 0, :time 1, :functions {:track8667 {:points [[0 0.5] [1 1]], :relative false, :final-points [[0 0.5] [1 1]]}}, :type :chip, :inputs nil, :outputs nil, :view {:offset [0 0], :zoom 1}, :final-time 1}, :block8666 {:children {:track8667 {:position [0.0 1.25 0.0], :rotation [0.0 1.0 0.0 0.0]}, :chip8669 {:position [0.0 0.0 0.3], :rotation [1.0 0.0 0.0 90.00000250447816]}, :chip8670 {:position [0.29999995 0.0 0.0], :rotation [0.0 0.0 -1.0 90.00000250447816]}}, :color :white, :scale [0.5 0.5 0.5], :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}}, :camera {:vector [0 0 1], :distance 40, :x-angle 37.79999999999996, :y-angle -58.99999999999952, :pivot [0.4108725494043277 -7.105427357601002E-15 -0.31054562731071655], :eye [27.502675146450226 24.51628112792968 15.967850979134596]}}