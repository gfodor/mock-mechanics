{:parts {:block18438 {:children {:cpu17631 {:position [-0.5 0.085 0.25], :rotation [0.0 1.0 0.0 0.0]}, :chip17632 {:position [0.0 0.085 0.25], :rotation [0.0 1.0 0.0 0.0]}, :chip17633 {:position [0.5 0.085 0.25], :rotation [0.0 1.0 0.0 0.0]}, :track17627 {:position [0.099999905 0.25 -0.25], :rotation [0.0 1.0 0.0 0.0]}, :block17647 {:position [-0.099999905 0.19999999 -0.25], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color [128 128 128], :pins nil, :scale (1.5 0.1 1.0), :value 0, :functions nil, :type :block, :layer 2, :connections nil}, :track9468 {:children {:block9469 {:position [0.0 0.25 0.3], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color [179 179 179], :pins nil, :scale [0.1 1.2 0.1], :value 0, :functions nil, :type :track, :layer 1, :connections nil}, :probe17634 {:children nil, :saved-value 0, :color :purple, :pins nil, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :probe, :layer 2, :connections nil}, :cpu17631 {:children nil, :saved-value 0, :color :blue, :pins {:probe17634 {:x 170, :trigger false, :value 0}, :chip17632 {:x 260, :trigger false, :value 0}, :chip17633 {:x 310, :trigger false, :value 0}, :button17623 {:x 90, :trigger true, :value 0}}, :scale [0.3 0.07 0.3], :gates {:gate-and17637 {:type :and, :x 170, :y 70, :tab 0}, :gate-not17641 {:type :not, :x 170, :y 50, :tab 1}, :gate-and17642 {:type :and, :x 170, :y 110, :tab 1}}, :value 0, :functions nil, :type :cpu, :layer 2, :connections {:connection17638 {:points [:probe17634 :gate-and17637], :tab 0}, :connection17639 {:points [:button17623 [90 70] :gate-and17637], :tab 0}, :connection17640 {:points [:gate-and17637 [260 70] :chip17632], :tab 0}, :connection17643 {:points [:button17623 [90 110] :gate-and17642], :tab 1}, :connection17644 {:points [:probe17634 :gate-not17641], :tab 1}, :connection17645 {:points [:gate-not17641 :gate-and17642], :tab 1}, :connection17646 {:points [:gate-and17642 [310 110] :chip17633], :tab 1}}, :tab 1}, :wagon17630 {:children {:probe17634 {:position [-0.125 -0.049999952 0.0], :rotation [0.0 0.0 1.0 90.00000250447816]}}, :saved-value 1.0, :color :yellow, :pins nil, :scale [0.15 0.15 0.15], :value 1.0, :functions nil, :track-lengths (0.5), :type :wagon, :loop-fn ([0.0 [0.0 -0.5 0.0]] [1.0 [0.0 0.0 0.0]]), :layer 2, :connections nil}, :probe17636 {:children nil, :saved-value 0, :color :purple, :pins nil, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :probe, :layer 2, :connections nil}, :block9386 {:children {:cylinder9387 {:position [0.0 0.0 1.1999999], :rotation [0.0 1.0 0.0 0.0]}, :probe9482 {:position [-0.10000008 -0.099999964 0.15000033], :rotation [1.0 -1.5911823E-7 -9.685755E-8 179.99997768819966]}}, :saved-value 0, :color [128 0 0], :pins nil, :scale (0.4 0.1 2.1), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :block9485-copy9487-copy9489 {:children nil, :saved-value 0, :color [0 0 0], :pins nil, :scale (0.1 0.1 0.19999999999999996), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :cylinder9387 {:children {:cylinder9388 {:position [0.0 0.20000005 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color :orange, :pins nil, :scale [1.0 0.4 1.0], :value 0, :functions nil, :type :cylinder, :layer 1, :connections nil}, :track17628 {:children {:wagon17630 {:position [0.0 0.0 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color [255 255 255], :pins nil, :scale [0.1 0.5 0.1], :value 0, :functions nil, :type :track, :layer 2, :connections nil}, :chip17632 {:children nil, :saved-value 0, :color :dark-gray, :pins nil, :scale [0.3 0.07 0.3], :value 0, :time 0.5, :functions {:wagon17630 {:points [[0 0] [0.5 0.5]], :relative false, :final-points [[0 0] [0.5 0.5]]}}, :type :chip, :layer 2, :connections nil, :view {:offset [0.16508940756320953 0.40147060751914976], :zoom-x 0.5, :zoom-y 0.5}, :final-time 0.5}, :block9469 {:children {:cylinder9470 {:position [0.0 0.0 0.65000004], :rotation [1.0 0.0 0.0 90.00000250447816]}, :chip9475 {:position [0.285 -0.005697727 0.31918922], :rotation [0.0 0.0 -1.0 90.00000250447816]}, :cpu9483 {:position [0.285 0.0015087128 -0.061591167], :rotation [0.0 0.0 -1.0 90.00000250447816]}}, :saved-value 0, :color :white, :pins nil, :scale (0.5 0.5 1.1), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :track9473 {:children {:block9474 {:position [-4.7683716E-7 -0.14999986 -0.29999924], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 6.416413, :color [255 0 0], :pins nil, :scale [0.1 0.30000000000000004 0.1], :free true, :value 6.416413, :functions nil, :type :track, :layer 1, :connections nil}, :track17627 {:children {:track17628 {:position [0.5 0.0 0.0], :rotation [0.0 0.0 -1.0 90.00000250447816]}}, :saved-value 0, :color :red, :pins nil, :scale [0.1 0.19999999999999996 0.1], :value 0, :functions nil, :type :track, :layer 2, :connections nil}, :block9485 {:children nil, :saved-value 0, :color [0 0 0], :pins nil, :scale (0.1 0.1 0.19999999999999996), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :cylinder9470 {:children {:cylinder9471 {:position [0.0 0.100000024 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color [0 0 0], :pins nil, :scale [2.9 0.19999999999999996 2.9], :value 0, :functions nil, :type :cylinder, :layer 1, :connections nil}, :cylinder9490 {:children nil, :saved-value 0, :color [0 0 0], :pins nil, :scale [0.6 0.1 0.6], :value 0, :functions nil, :type :cylinder, :layer 1, :connections nil}, :block17647 {:children {:probe17636 {:position [0.049999952 0.2 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color :white, :pins nil, :scale (0.19999999999999996 0.29999999999999993 0.19999999999999996), :value 0, :functions nil, :type :block, :layer 2, :connections nil}, :cylinder9471 {:children {:track9473 {:position [0.0 0.35000008 0.0], :rotation [0.0 1.0 0.0 0.0]}, :block9485 {:position [0.0 0.100000024 1.1500001], :rotation [0.0 1.0 0.0 0.0]}, :block9485-copy9486 {:position [0.0 0.100000024 -1.1999998], :rotation [0.0 1.0 0.0 0.0]}, :block9485-copy9487 {:position [-1.2 0.100000024 0.0], :rotation [0.0 1.0 0.0 90.00000250447816]}, :block9485-copy9487-copy9489 {:position [1.2 0.100000024 0.0], :rotation [1.1920929E-7 1.0 0.0 90.00000250447816]}, :cylinder9490 {:position [0.0 0.05000007 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0, :color [255 255 255], :pins nil, :scale [2.7 0.1 2.7], :value 0, :functions nil, :type :cylinder, :layer 1, :connections nil}, :cpu17648 {:children nil, :saved-value 0, :color :blue, :pins {:probe17634 {:x 130, :trigger true, :value 0}, :chip9389 {:x 230, :trigger false, :value 0}}, :script true, :scale [0.3 0.07 0.3], :value 0, :functions nil, :type :cpu, :layer 1, :connections {:connection17649 {:points [:probe17634 :chip9389], :tab 0}}, :tab 0}, :probe9482 {:children nil, :saved-value 0, :color :purple, :pins nil, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :probe, :layer 1, :connections nil}, :ground-part {:children {:block18438 {:position [2.25 0.15 -0.5], :rotation [0.0 1.0 0.0 180.00000500895632]}, :block9384 {:position [-0.25 1.5 -0.25], :rotation [0.0 1.0 0.0 0.0]}, :button17623 {:position [0.25 0.2 -0.25], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value nil, :color :dark-gray, :pins nil, :scale [12 0.2 12], :functions nil, :type :ground, :connections nil}, :chip17633 {:children nil, :saved-value 0, :color :dark-gray, :pins nil, :scale [0.3 0.07 0.3], :value 0, :time 0.5, :functions {:wagon17630 {:points [[0 0.5] [0.5 0.0]], :relative false, :final-points [[0 0.5] [0.5 0.0]]}}, :type :chip, :layer 2, :connections nil, :view {:offset [0.1680700570344925 0.34264705181121824], :zoom-x 0.5, :zoom-y 0.5}, :final-time 0.5}, :track9385 {:children {:block9386 {:position [0.0 0.049999952 0.70000005], :rotation [0.0 1.0 0.0 0.0]}}, :saved-value 0.0, :color [179 179 179], :pins nil, :scale [0.1 0.3999999999999999 0.1], :value 0.0, :functions nil, :type :track, :layer 1, :connections nil}, :chip9389 {:children nil, :saved-value 0, :color :dark-gray, :pins nil, :scale [0.3 0.07 0.3], :value 0, :time 2.0, :functions {:track9385 {:points [[0 0] [0.5 0.12] [1.0 0.0] [1.5 -0.12] [2.0 0.0]], :relative false, :final-points [[0 0] [0.5 0.12] [1.0 0.0] [1.5 -0.12] [2.0 0.0]]}}, :type :chip, :layer 1, :connections nil, :view {:offset [0.21504140949721937 0.44254077043187756], :zoom-x 0.1414263167214047, :zoom-y 3.463751370540111}, :final-time 2.0}, :button17623 {:children nil, :saved-value 0, :color :red, :pins nil, :scale [0.5 0.2 0.5], :value 0, :functions nil, :type :button, :layer 1, :connections nil}, :cpu9483 {:children nil, :saved-value 0, :color :blue, :pins {:probe9480 {:x 120, :trigger true, :value 0}, :chip9475 {:x 200, :trigger false, :value 0}}, :scale [0.3 0.07 0.3], :gates {}, :value 0, :functions nil, :type :cpu, :layer 1, :connections {:connection9484 {:points [:probe9480 :chip9475], :tab 0}}, :tab 0}, :cylinder9388 {:children nil, :saved-value 0, :color [128 0 0], :pins nil, :scale [0.6 0.1 0.6], :value 0, :functions nil, :type :cylinder, :layer 1, :connections nil}, :block9485-copy9487 {:children nil, :saved-value 0, :color [0 0 0], :pins nil, :scale (0.1 0.1 0.19999999999999996), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :cone9476 {:children nil, :saved-value 0, :color [0 0 0], :pins nil, :scale (0.3 0.3 0.3), :value 0, :functions nil, :type :cone, :layer 1, :connections nil}, :chip9475 {:children nil, :saved-value 0, :color :dark-gray, :pins nil, :scale [0.3 0.07 0.3], :value 0, :time 0.08333, :functions {:track9473 {:points [[0 1.0] [0.08333 1.0]], :relative true, :final-points [[0 6.333082675933838] [0.08333 6.416412675933838]]}}, :type :chip, :layer 1, :connections nil, :view {:offset [0.025 0.1], :zoom-x 0.5, :zoom-y 0.5}, :final-time 0.08333}, :block9384 {:children {:track9385 {:position [0.0 1.15 0.65], :rotation [1.0 0.0 0.0 90.00000250447816]}, :chip9389 {:position [0.0 1.0000001 -0.28500003], :rotation [-1.0 0.0 0.0 90.00000250447816]}, :track9468 {:position [0.0 2.6 0.0], :rotation [0.0 1.0 0.0 0.0]}, :block9479 {:position [0.4 0.39999998 0.15], :rotation [0.0 0.0 -1.0 90.00000250447816]}, :cpu17648 {:position [0.0 0.5 -0.28500003], :rotation [-1.0 0.0 0.0 90.00000250447816]}}, :saved-value 0, :color :white, :pins nil, :scale (0.5 2.8000000000000003 0.5), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :block9479 {:children {:probe9480 {:position [-0.100000024 0.099999964 0.45000002], :rotation [1.0 -1.1920929E-7 -2.3841858E-7 90.00000250447816]}}, :saved-value 0, :color [51 51 51], :pins nil, :scale (0.3 0.3 0.8), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :block9485-copy9486 {:children nil, :saved-value 0, :color [0 0 0], :pins nil, :scale (0.1 0.1 0.19999999999999996), :value 0, :functions nil, :type :block, :layer 1, :connections nil}, :probe9480 {:children nil, :saved-value 0, :color :purple, :pins nil, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :probe, :layer 1, :connections nil}, :block9474 {:children {:cone9476 {:position [8.6832047E-4 -0.0061036944 -0.70000005], :rotation [-1.0 1.3248662E-7 7.9060285E-8 90.00000250447816]}}, :saved-value 0, :color [0 0 0], :pins nil, :scale (0.19999999999999998 0.1 1.1), :value 0, :functions nil, :type :block, :layer 1, :connections nil}}, :camera {:vector [0 0 1], :distance 50, :x-angle 25.400000000000045, :y-angle -1466.6000000000247, :pivot [0.4106215042727334 2.231965177825519 0.17089986960802758], :eye [20.634425978149686 23.678721540740558 40.55696249167834]}, :visible-layers [1]}