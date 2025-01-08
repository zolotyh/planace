(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.ui :as ui]
   [com.biffweb :as biff]
   [reitit.core :as r]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]))

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

(defn room-list-item [{:keys [reitit.core/router]} room]
  (let [path (:path
              (r/match-by-name router paths-ids/room {:room-id (:xt/id room)}))]
    [:li
     [:a {:href path :hx-get path :hx-target ids/root-id} (cheshire/generate-string room {:pretty true})]]))

(defn room-list [room-list ctx]
  (ui/page {:title "room-list"}
           (room-create-form {:path "path"})
           [:ul (map (partial room-list-item ctx) room-list)]))

(defn room [{:keys [votes options room]}]
  (let [{:keys [room/title closed?]} room]
    (ui/page
     {:title "room page"}
     [:div {:id ids/room}
      [:h1
       title]
      [:h3 (if closed? true false)]
      (voting options)
      (results votes)])))
