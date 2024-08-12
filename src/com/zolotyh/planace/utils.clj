(ns com.zolotyh.planace.utils)

(defn sort-by-value
  "Sort map by value"
  [m]
  (->> m
       (seq)
       (sort-by second)
       (into {})))

(defn list-to-hash-map
  "Convert list to hash map where keys are equal to values"
  [arr]
  (reduce (fn [acc elem] (assoc acc elem elem)) (sorted-map) arr))
