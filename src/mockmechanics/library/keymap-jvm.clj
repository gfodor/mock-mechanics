(ns mockmechanics.library.keymap
  (:import org.lwjgl.glfw.GLFW))

(def keymap {GLFW/GLFW_KEY_SPACE                   " "
             GLFW/GLFW_KEY_APOSTROPHE              "'"
             GLFW/GLFW_KEY_COMMA                   ","
             GLFW/GLFW_KEY_MINUS                   "-"
             GLFW/GLFW_KEY_PERIOD                  "."
             GLFW/GLFW_KEY_SLASH                   "/"
             GLFW/GLFW_KEY_0                       "0"
             GLFW/GLFW_KEY_1                       "1"
             GLFW/GLFW_KEY_2                       "2"
             GLFW/GLFW_KEY_3                       "3"
             GLFW/GLFW_KEY_4                       "4"
             GLFW/GLFW_KEY_5                       "5"
             GLFW/GLFW_KEY_6                       "6"
             GLFW/GLFW_KEY_7                       "7"
             GLFW/GLFW_KEY_8                       "8"
             GLFW/GLFW_KEY_9                       "9"
             GLFW/GLFW_KEY_SEMICOLON               ";"
             GLFW/GLFW_KEY_EQUAL                   "="
             GLFW/GLFW_KEY_A                       "a"
             GLFW/GLFW_KEY_B                       "b"
             GLFW/GLFW_KEY_C                       "c"
             GLFW/GLFW_KEY_D                       "d"
             GLFW/GLFW_KEY_E                       "e"
             GLFW/GLFW_KEY_F                       "f"
             GLFW/GLFW_KEY_G                       "g"
             GLFW/GLFW_KEY_H                       "h"
             GLFW/GLFW_KEY_I                       "i"
             GLFW/GLFW_KEY_J                       "j"
             GLFW/GLFW_KEY_K                       "k"
             GLFW/GLFW_KEY_L                       "l"
             GLFW/GLFW_KEY_M                       "m"
             GLFW/GLFW_KEY_N                       "n"
             GLFW/GLFW_KEY_O                       "o"
             GLFW/GLFW_KEY_P                       "p"
             GLFW/GLFW_KEY_Q                       "q"
             GLFW/GLFW_KEY_R                       "r"
             GLFW/GLFW_KEY_S                       "s"
             GLFW/GLFW_KEY_T                       "t"
             GLFW/GLFW_KEY_U                       "u"
             GLFW/GLFW_KEY_V                       "v"
             GLFW/GLFW_KEY_W                       "w"
             GLFW/GLFW_KEY_X                       "x"
             GLFW/GLFW_KEY_Y                       "y"
             GLFW/GLFW_KEY_Z                       "z"
             GLFW/GLFW_KEY_LEFT_BRACKET            "["
             GLFW/GLFW_KEY_BACKSLASH               nil
             GLFW/GLFW_KEY_RIGHT_BRACKET           "]"
             GLFW/GLFW_KEY_ESCAPE                  :esc
             GLFW/GLFW_KEY_GRAVE_ACCENT            :esc
             GLFW/GLFW_KEY_ENTER                   :enter
             GLFW/GLFW_KEY_TAB                     nil
             GLFW/GLFW_KEY_BACKSPACE               :backspace
             GLFW/GLFW_KEY_INSERT                  nil
             GLFW/GLFW_KEY_DELETE                  :delete
             GLFW/GLFW_KEY_RIGHT                   :right
             GLFW/GLFW_KEY_LEFT                    :left
             GLFW/GLFW_KEY_DOWN                    :down
             GLFW/GLFW_KEY_UP                      :up
             GLFW/GLFW_KEY_PAGE_UP                 nil
             GLFW/GLFW_KEY_PAGE_DOWN               nil
             GLFW/GLFW_KEY_HOME                    nil
             GLFW/GLFW_KEY_END                     nil
             GLFW/GLFW_KEY_F1                      nil
             GLFW/GLFW_KEY_F2                      nil
             GLFW/GLFW_KEY_F3                      nil
             GLFW/GLFW_KEY_F4                      nil
             GLFW/GLFW_KEY_F5                      nil
             GLFW/GLFW_KEY_F6                      nil
             GLFW/GLFW_KEY_F7                      nil
             GLFW/GLFW_KEY_F8                      nil
             GLFW/GLFW_KEY_F9                      nil
             GLFW/GLFW_KEY_F10                     nil
             GLFW/GLFW_KEY_F11                     nil
             GLFW/GLFW_KEY_F12                     nil
             GLFW/GLFW_KEY_KP_DECIMAL              nil
             GLFW/GLFW_KEY_KP_DIVIDE               nil
             GLFW/GLFW_KEY_KP_MULTIPLY             nil
             GLFW/GLFW_KEY_KP_SUBTRACT             nil
             GLFW/GLFW_KEY_KP_ADD                  nil
             GLFW/GLFW_KEY_KP_ENTER                nil
             GLFW/GLFW_KEY_KP_EQUAL                nil
             GLFW/GLFW_KEY_LEFT_SHIFT              :shift
             GLFW/GLFW_KEY_RIGHT_SHIFT             :shift
             GLFW/GLFW_KEY_LEFT_CONTROL            :control
             GLFW/GLFW_KEY_RIGHT_CONTROL           :control
             GLFW/GLFW_KEY_LEFT_ALT                :alt
             GLFW/GLFW_KEY_RIGHT_ALT               :alt
             GLFW/GLFW_KEY_LEFT_SUPER              :alt
             GLFW/GLFW_KEY_RIGHT_SUPER             :alt
             })

(def shift-keymap {GLFW/GLFW_KEY_APOSTROPHE              "@"
                   GLFW/GLFW_KEY_COMMA                   "<"
                   GLFW/GLFW_KEY_MINUS                   "_"
                   GLFW/GLFW_KEY_PERIOD                  ">"
                   GLFW/GLFW_KEY_SLASH                   "?"
                   GLFW/GLFW_KEY_0                       ")"
                   GLFW/GLFW_KEY_1                       "!"
                   GLFW/GLFW_KEY_2                       "\""
                   GLFW/GLFW_KEY_3                       "£"
                   GLFW/GLFW_KEY_4                       "$"
                   GLFW/GLFW_KEY_5                       "%"
                   GLFW/GLFW_KEY_6                       "^"
                   GLFW/GLFW_KEY_7                       "&"
                   GLFW/GLFW_KEY_8                       "*"
                   GLFW/GLFW_KEY_9                       "("
                   GLFW/GLFW_KEY_SEMICOLON               ":"
                   GLFW/GLFW_KEY_EQUAL                   "+"
                   GLFW/GLFW_KEY_A                       "A"
                   GLFW/GLFW_KEY_B                       "B"
                   GLFW/GLFW_KEY_C                       "C"
                   GLFW/GLFW_KEY_D                       "D"
                   GLFW/GLFW_KEY_E                       "E"
                   GLFW/GLFW_KEY_F                       "F"
                   GLFW/GLFW_KEY_G                       "G"
                   GLFW/GLFW_KEY_H                       "H"
                   GLFW/GLFW_KEY_I                       "I"
                   GLFW/GLFW_KEY_J                       "J"
                   GLFW/GLFW_KEY_K                       "K"
                   GLFW/GLFW_KEY_L                       "L"
                   GLFW/GLFW_KEY_M                       "M"
                   GLFW/GLFW_KEY_N                       "N"
                   GLFW/GLFW_KEY_O                       "O"
                   GLFW/GLFW_KEY_P                       "P"
                   GLFW/GLFW_KEY_Q                       "Q"
                   GLFW/GLFW_KEY_R                       "R"
                   GLFW/GLFW_KEY_S                       "S"
                   GLFW/GLFW_KEY_T                       "T"
                   GLFW/GLFW_KEY_U                       "U"
                   GLFW/GLFW_KEY_V                       "V"
                   GLFW/GLFW_KEY_W                       "W"
                   GLFW/GLFW_KEY_X                       "X"
                   GLFW/GLFW_KEY_Y                       "Y"
                   GLFW/GLFW_KEY_Z                       "Z"
                   GLFW/GLFW_KEY_LEFT_BRACKET            "{"
                   GLFW/GLFW_KEY_RIGHT_BRACKET           "}"
                   })
