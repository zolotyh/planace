(ns com.zolotyh.planace.utils.sequences
  (:require
   [com.zolotyh.planace.utils.collections :refer [list-to-hash-map
                                                  sort-by-value]]))

(def t-shirts (sorted-map
               :xs 13
               :s 21
               :m 34
               :l 55
               :xl 89
               :xxl 144))

(def fib (lazy-cat [0 1] (map + fib (rest fib))))

(def sequences {:fib (list-to-hash-map (take 10 fib))
                :natural (list-to-hash-map (range 0 10))
                :t-shirts (sort-by-value t-shirts)})
