(ns com.zolotyh.planace.math
  (:require
   [kixi.stats.core :refer [median mean]]))

(def sequences {:fib {0 0 1 1 2 2 3 3 5 5 8 8 13 13}
                :simple {1 1 2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 10}
                :t-shirts {1 "xs" 10 "s" 100 "m" 1000 "l" 10000 "xl" 1000000 "xxl"}})

(defn get-closer [to-compare a1 a2]
  (let [a1-diff (abs (- a1 to-compare))
        a2-diff (abs (- a2 to-compare))]
    (cond (< a1-diff a2-diff) a1
          (= a1-diff a2-diff) (min a1 a2)
          :else a2)))

(defn get-closest [i arr]
  (let [reducer (partial get-closer i)]
    (reduce reducer arr)))

;; (transduce identity median [1 1 1 2 2 2])
