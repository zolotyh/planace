(ns com.zolotyh.planace.poker.ui
  (:require
   [com.zolotyh.planace.poker.ids :as ids]
   [cheshire.core :as cheshire]))

(defn hx-vote-attrs [{:keys [path]}]
  {:hx-post path
   :hx-target ids/room-id
   :hx-ext "json-enc"
   :hx-trigger "click"})

(defn voting-panel-item [{:keys [key] :as item} ctx]
  (let [item-json (cheshire/generate-string item)]
    [:div (merge (hx-vote-attrs ctx) {:hx-val item-json}) key]))

(defn voting-panel [seq {:keys [path]}]
  [:div
   (map #(voting-panel-item % {:path path})  seq)])

(voting-panel
 (->>
  (range 2)
  (map #(hash-map :key % :value %)))
 {:path "path to"})

(defn results-panel []
  [:div "results-panel"])

(defn room []
  [:div "room"
   (voting-panel)
   (results-panel)])



