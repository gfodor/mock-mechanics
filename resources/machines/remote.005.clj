{:ground-children {:ground-part {:position [0.0 0.0 0.0], :rotation [-1.0 0.0 0.0 90.00000250447816]}}, :parts {:ground-part {:type :ground, :color [40 40 40], :value 0, :scale [12 12 1], :children {:block8356 {:position [0.5 -1.25 0.25], :rotation [1.0 0.0 0.0 90.00000250447816]}}}, :block8356 {:type :block, :color :white, :value 0, :scale (1.0 0.5 1.5), :children {:lamp8357 {:position [-0.25 0.35000002 -0.5], :rotation [0.0 1.0 0.0 0.0]}, :lamp8358 {:position [0.25 0.35000002 -0.5], :rotation [0.0 1.0 0.0 0.0]}, :button8360 {:position [-0.25 0.35000002 0.5], :rotation [0.0 1.0 0.0 0.0]}, :cpu10126 {:position [0.25 0.3 0.5], :rotation [0.0 1.0 0.0 0.0]}}}, :lamp8357 {:type :lamp, :color [255 0 0], :value 1, :scale [0.2 0.2 0.2], :dark-color [127 0 0], :children nil}, :lamp8358 {:type :lamp, :color [0 255 0], :value 0, :scale [0.2 0.2 0.2], :dark-color [0 127 0], :children nil}, :button8360 {:type :button, :color [51 51 51], :value 0, :scale [0.5 0.2 0.5], :children nil}, :cpu10126 {:children nil, :color :blue, :pins {:lamp8357 {:x 240, :trigger false, :value 1}, :lamp8358 {:x 320, :trigger false, :value 0}, :button8360 {:x 100, :trigger false, :value 0}}, :scale [0.3 0.1 0.3], :gates {:gate-not10131 {:type :not, :x 100, :y 540, :tab 0}}, :value 0, :type :cpu, :connections {:connection18292 {:points [:button8360 :gate-not10131], :tab 0}, :connection18293 {:points [:gate-not10131 [320 540] :lamp8358], :tab 0}, :connection18294 {:points [:button8360 :lamp8357], :tab 0}}, :tab 0}}, :camera {:vector [0 0 1], :distance 27.933491843749998, :x-angle 45.00000000000008, :y-angle -24.999999999999954, :pivot [1.0394417940639302 0.25 1.792215807725885], :eye [9.386981695797328 20.001962661743164 19.69357345848272]}}