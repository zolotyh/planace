(ns com.zolotyh.planace.poker.ctrls
  (:require
   [com.zolotyh.planace.poker.ui :as ui]))

(def test-data {:votes
                (->>
                 (range 2)
                 (map #(hash-map :val % :user (random-uuid))))
                :options
                (->>
                 (range 3)
                 (map #(hash-map :val % :keys %)))
                :room {:title "<>room title<> "
                       :closed? true}})

(defn room [_]
  (ui/room test-data))
