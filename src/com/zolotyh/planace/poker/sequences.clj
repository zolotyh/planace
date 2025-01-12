(ns com.zolotyh.planace.poker.sequences)

(defn natural
  ([n] (->> (range n)
            (map #(hash-map :val % :key %))))
  ([]
   (natural 10))) ;default value is 10

(defn fib
  ([n]
   (->> [0 1]
        (iterate (fn [[a b]] [b (+ a b)]))
        (map first)
        (take n)
        (distinct)
        (map #(hash-map :key % :val %))))
  ([] (fib 10))) ;default value is 10

(defn t-shirts []
  [{:key "0"      :val 0}
   {:key "xs",    :val 10}
   {:key "s",     :val 100}
   {:key "m",     :val 1000}
   {:key "l",     :val 10000}
   {:key "xl",    :val 10000}
   {:key "xxl",   :val 100000}
   {:key "xxxl",  :val 1000000}
   {:key "xxxxl", :val 10000000}])
