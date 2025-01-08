(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.poker.ids :as ids]))

(defn results-item [item]
  [:li
   (cheshire/generate-string  item {:pretty true})])

(defn results [votes]
  [:div (map results-item votes)])

(defn voting-item [item]
  [:li (cheshire/generate-string item {:pretty true})])

(defn voting [options]
  [:div (map voting-item options)])

(defn room [{:keys [votes options room]}]
  (let [{:keys [title closed?]} room]
    [:div {:id ids/room}
     [:h1
      title]
     [:h3 (if closed? true false)]
     (voting options)
     (results votes)]))
