{:ground-children {:block7524 {:position [0.75 0.25 0.25], :rotation [0.0 1.0 0.0 0.0]}}, :parts {:block7524 {:children {:track7525 {:position [0.0 1.25 0.0], :rotation [0.0 1.0 0.0 0.0]}, :chip7529 {:position [0.29999995 0.0 0.0], :rotation [0.0 0.0 -1.0 90.00000250447816]}, :chip7530 {:position [0.0 0.0 -0.3], :rotation [-1.0 0.0 0.0 90.00000250447816]}, :cpu7531 {:position [0.0 0.0 0.3], :rotation [1.0 0.0 0.0 90.00000250447816]}}, :color :white, :scale [0.5 0.5 0.5], :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :track7525 {:children {:block7526 {:position [0.65 0.049999952 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :color :red, :scale [0.1 1 0.1], :value 1.0, :functions nil, :type :track, :inputs nil, :outputs nil}, :block7526 {:children {:track7527 {:position [0.0 1.05 0.0], :rotation [0.0 1.0 0.0 0.0]}}, :color :yellow, :scale (1.8 0.1 0.5), :value 0, :functions nil, :type :block, :inputs nil, :outputs nil}, :track7527 {:children nil, :color :green, :scale [0.1 1 0.1], :value 0.0, :functions nil, :type :track, :inputs nil, :outputs nil}, :chip7529 {:children nil, :color :gray, :scale [0.3 0.1 0.3], :value 0, :time 1, :functions {:track7525 {:points [[0 0] [1 1]], :relative false, :final-points [[0 0] [1 1]]}}, :type :chip, :inputs nil, :outputs nil, :view {:offset [0 0], :zoom 0.95}, :final-time 1}, :chip7530 {:children nil, :color :gray, :scale [0.3 0.1 0.3], :value 0, :time 1.0, :functions {:track7527 {:points [[0 1.0] [1.0 0.0]], :relative false, :final-points [[0 1.0] [1.0 0.0]]}}, :type :chip, :inputs nil, :outputs nil, :view {:offset [0.08643817156553268 0.1838234961032868], :zoom 0.7499999999999998}, :final-time 1.0}, :cpu7531 {:children nil, :color :blue, :scale [0.3 0.1 0.3], :root-filename "test", :value 0, :functions nil, :type :cpu, :inputs nil, :outputs {:chip7529 0, :chip7530 0}}}}