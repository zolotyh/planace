(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.ui :as ui]
   [com.biffweb :as biff]))

(defn results-item [item]
  [:li
   (cheshire/generate-string  item {:pretty true})])

(defn results [votes]
  [:div (map results-item votes)])

(defn voting-item [item]
  [:li (cheshire/generate-string item {:pretty true})])

(defn voting [options]
  [:div (map voting-item options)])

(defn room-create-form [{:keys [path]}]
  (biff/form
   {:src path :method "post" :hx-post true}
   [:input {:type "text" :name "title"}]
   [:button {:type "submit"} "Create new room"]))

(defn room-list-item [room]
  [:li (cheshire/generate-string room {:pretty true})])

(defn room-list [room-list]
  (ui/page {:title "room-list"}
           (room-create-form {:path "path"})
           [:ul (map room-list-item room-list)]))

(defn room [{:keys [votes options room]}]
  (let [{:keys [title closed?]} room]
    (ui/page
     {:title "room page"}
     [:div {:id ids/room}
      [:h1
       title]
      [:h3 (if closed? true false)]
      (voting options)
      (results votes)])))
