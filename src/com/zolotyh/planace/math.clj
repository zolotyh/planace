(ns com.zolotyh.planace.math)

(def sequences {:simple #{0 1 2 3 4 5 6 7 8 9 10}
                :fib #{0 1 2 3 5 8 13 21}
                :tshorts #{"not defined" "xs" "s" "m" "l" "xl" "xxl"}})

(defn mean [coll]
  (let [sum (apply + coll)
        count (count coll)]
    (if (pos? count)
      (/ sum count)
      0)))

(mean #{0 1 2 3 4 5 6 7 8 9 10})

(mean [1 2 3])
