(ns com.zolotyh.planace.ui.pocker
  (:require
   [com.zolotyh.planace.math :refer [sequences]]))

(defn card [value]
  [:button.px-2.py-1.bg-orange-500 {:_ (str "on click toggle .{'!bg-blue-500'} on me")} value])

(defn cards [type]
  (map card (type sequences)))

(cards :fib)
(cards :simple)
(cards :tshorts)

