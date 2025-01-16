(ns ti-dice-roll.styles
  (:require-macros
   [garden.def :refer [defcssfn]])
  (:require
   [spade.core   :refer [defglobal defclass]]
   [garden.units :refer [deg px]]
   [garden.color :refer [rgba]]))

(defglobal defaults
  [:body
   {:color               :dark-gray
    :background-color    :#ddd}])

(defclass level1
  []
  {:color :dark-gray})
