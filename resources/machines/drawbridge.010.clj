{:ground-children {:ground-part {:position [0.0 0.0 0.0], :rotation [-1.0 0.0 0.0 90.00000250447816]}}, :parts {:chip24664 {:children nil, :color :gray, :scale [0.3 0.1 0.3], :value 0, :time 1.0, :functions {:track24662 {:points [[0 0] [1.0 0.1]], :relative false, :final-points [[0 0] [1.0 0.1]]}, :track24662-copy25591 {:points [[0 0] [1.0 0.1]], :relative false, :final-points [[0 0] [1.0 0.1]]}}, :type :chip, :inputs nil, :hidden true, :outputs nil, :view {:offset [0 0], :zoom 1}, :final-time 1.0}, :probe24668 {:children nil, :color :purple, :scale [0.1 0.1 0.1], :value 1, :functions nil, :type :probe, :inputs nil, :outputs nil}, :block24663-copy25591 {:children {:track30835 {:position [-1.3000001 0.9 0.050000012], :rotation [0.0 1.0 0.0 0.0]}}, :color [0 0 255], :scale (2.9000000000000004 1.6 0.1), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :block24661 {:children {:track24662 {:position [-0.6 0.45 0.35000002], :rotation [1.0 0.0 0.0 90.00000250447816]}, :probe24668 {:position [0.5 0.39999998 0.3], :rotation [1.0 0.0 0.0 90.00000250447816]}, :button24669 {:position [-0.39999986 0.65000004 0.0], :rotation [0.0 1.0 0.0 0.0]}, :chip24665 {:position [0.0 0.19999999 -0.3], :rotation [-1.0 0.0 0.0 90.00000250447816]}, :chip24664 {:position [0.39999998 0.19999999 -0.3], :rotation [-1.0 0.0 0.0 90.00000250447816]}, :cpu16238 {:position [-0.45000005 0.19999999 -0.3], :rotation [-1.0 0.0 0.0 90.00000250447816]}}, :color :white, :scale (1.5 1.1 0.5), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :block24661-copy29352 {:children nil, :color :white, :scale (1.5 1.1 0.5), :value 0, :functions nil, :type :block, :inputs nil, :hidden false, :outputs nil}, :ground-part {:children {:block24661 {:position [-2.25 0.25 0.55], :rotation [1.0 0.0 0.0 90.00000250447816]}, :block24661-copy25591 {:position [2.05 -2.05 0.55], :rotation [-3.090862E-8 0.70710677 0.70710677 180.00000500895632]}, :block24661-copy29352 {:position [-2.25 -2.05 0.55], :rotation [1.0 0.0 0.0 90.00000250447816]}, :block24661-copy29352-copy29794 {:position [2.05 0.25 0.55], :rotation [1.0 0.0 0.0 90.00000250447816]}}, :color [40 40 40], :scale [12 12 1], :value 0, :functions nil, :type :ground, :inputs nil, :outputs nil}, :block24661-copy25591 {:children {:track24662-copy25591 {:position [-0.6 0.45 0.35000002], :rotation [1.0 0.0 0.0 90.00000250447816]}}, :color :white, :scale (1.5 1.1 0.5), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :probe24667 {:children nil, :color :purple, :scale [0.1 0.1 0.1], :value 1, :functions nil, :type :probe, :inputs nil, :outputs nil}, :track24662 {:children {:block24663 {:position [1.3000001 0.79999995 -0.049999952], :rotation [0.0 1.0 0.0 0.0]}}, :color :red, :scale [0.1 0.1 0.1], :value 0.0, :functions nil, :type :track, :inputs nil, :outputs nil}, :block24661-copy29352-copy29794 {:children nil, :color :white, :scale (1.5 1.1 0.5), :value 0, :functions nil, :type :block, :inputs nil, :hidden false, :outputs nil}, :block24663 {:children {:probe24667 {:position [-0.20000005 -0.75 0.100000024], :rotation [1.0 0.0 0.0 90.00000250447816]}, :track31036 {:position [-1.3000001 0.9000001 0.050000012], :rotation [0.0 1.0 0.0 0.0]}}, :color [0 145 0], :scale (2.9000000000000004 1.6 0.1), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :cpu16238 {:children nil, :color :blue, :pins {:button24669 {:x 80, :trigger true, :value 0}, :chip24665 {:x 360, :trigger false, :value 0}, :chip24664 {:x 260, :trigger false, :value 0}, :probe24667 {:x 160, :trigger false, :value 0}}, :scale [0.3 0.1 0.3], :gates {:gate-and18527 {:type :and, :x 160, :y 560, :tab 0}, :gate-not18531 {:type :not, :x 160, :y 520, :tab 1}, :gate-and18532 {:type :and, :x 160, :y 580, :tab 1}}, :value 0, :type :cpu, :hidden true, :connections {:connection18528 {:points [:button24669 [80 560] :gate-and18527], :tab 0}, :connection18529 {:points [:probe24667 :gate-and18527], :tab 0}, :connection18530 {:points [:gate-and18527 [260 560] :chip24664], :tab 0}, :connection18533 {:points [:button24669 [80 580] :gate-and18532], :tab 1}, :connection18534 {:points [:probe24667 :gate-not18531], :tab 1}, :connection18535 {:points [:gate-not18531 :gate-and18532], :tab 1}, :connection18536 {:points [:gate-and18532 [360 580] :chip24665], :tab 1}}, :tab 1}, :button24669 {:children nil, :color :red, :scale [0.5 0.2 0.5], :value 0, :functions nil, :type :button, :inputs nil, :outputs nil}, :chip24665 {:children nil, :color :gray, :scale [0.3 0.1 0.3], :value 0, :time 1.0, :functions {:track24662 {:points [[0 0.1] [1.0 0.0]], :relative false, :final-points [[0 0.1] [1.0 0.0]]}, :track24662-copy25591 {:points [[0 0.1] [1.0 0.0]], :relative false, :final-points [[0 0.1] [1.0 0.0]]}}, :type :chip, :inputs nil, :hidden true, :outputs nil, :view {:offset [0.29210132732987404 0.5441176742315292], :zoom 1}, :final-time 1.0}, :track30835 {:children nil, :color :red, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :track, :inputs nil, :outputs nil}, :track24662-copy25591 {:children {:block24663-copy25591 {:position [1.3000001 0.79999995 -0.049999952], :rotation [0.0 1.0 0.0 0.0]}}, :color :red, :scale [0.1 0.1 0.1], :value 0.0, :functions nil, :type :track, :inputs nil, :outputs nil}, :track31036 {:children nil, :color :red, :scale [0.1 0.1 0.1], :value 0, :functions nil, :type :track, :inputs nil, :outputs nil}}, :camera {:vector [0 0 1], :distance 59.01567442090857, :x-angle 32.2000000000002, :y-angle -144.99999999997462, :pivot [0.005387060776666175 0.0 1.0155260982356396], :eye [28.64902567161651 31.448055267333984 -39.89183016397139]}}