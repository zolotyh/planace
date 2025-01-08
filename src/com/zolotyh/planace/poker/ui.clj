(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.poker.ids :as ids]))

(defn results [votes]
  [:div (cheshire/generate-string  votes {:pretty true})])

(defn voting [options]
  [:div (cheshire/generate-string  options {:pretty true})])

(defn room [{:keys [votes options room]}]
  (let [{:keys [title]} room]
    [:div {:id ids/room}
     title
     (voting options)
     (results votes)]))

(room {:votes
       (->>
        (range 2)
        (map #(hash-map :val % :user (random-uuid))))
       :options
       (->>
        (range 3)
        (map #(hash-map :val % :keys %)))
       :room {:title "room title"}})
