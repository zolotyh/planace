(ns com.zolotyh.planace.utils.sequences
  (:require
   [clojure.set :refer [map-invert]]
   [com.zolotyh.planace.utils.collections :refer [list-to-hash-map
                                                  sort-by-value]]
   [kixi.stats.core :refer [mean median]]))
(def vote-fn {:mean #(transduce identity mean %)
              :median #(transduce identity median %)})

(def t-shirts (sorted-map
               "xs"  13
               "s" 21
               "m" 34
               "l" 55
               "xl" 89
               "xxl" 144))

(def fib (lazy-cat [0 1] (map + fib (rest fib))))

(def sequences {:fib (list-to-hash-map (take 10 fib))
                :natural (list-to-hash-map (range 0 10))
                :t-shirts (sort-by-value t-shirts)})

(defn get-closer [to-compare a1 a2]
  (let [a1-diff (abs (- a1 to-compare))
        a2-diff (abs (- a2 to-compare))]
    (cond (< a1-diff a2-diff) a1
          (= a1-diff a2-diff) (max a1 a2)
          :else a2)))

(defn get-closest [i arr]
  (let [reducer (partial get-closer i)]
    (reduce reducer arr)))

(defn get-vote-result [votes vote-fn-type sequence-type]
  (let [result ((vote-fn vote-fn-type) votes)
        active-sequence (sequences sequence-type)
        sequence-values (vals active-sequence)]
    (let [vote (get-closest result sequence-values)]
      {:key
       ((map-invert active-sequence)
        vote)
       :value vote})))

(get-vote-result [5 5 5 5 5 7 8 8] :median :fib)
