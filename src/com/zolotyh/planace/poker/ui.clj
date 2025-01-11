(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.ui :as ui]
   [com.biffweb :as biff]
   [reitit.core :as r]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]
   [clojure.tools.logging :as log]
   [rum.core :as rum]))

(defn results-item [item]
  [:li
   [:a {:href "test" :hx-post "test" :hx-trigger "click"}
    (cheshire/generate-string  item {:pretty true})]])

(defn results [votes]
  [:<>
   [:h2 "results"]
   [:ul (map results-item votes)]])

(defn voting-item [{:keys [reitit.core/router path-params]} item]
  (let [room-id (:room-id path-params)
        url (:path
             (r/match-by-name router paths-ids/vote {:room-id room-id}))]
    (log/error "url is" url (r/match-by-name router paths-ids/vote {:room-id room-id}))
    [:li
     [:a {:hx-post url
          :hx-target ids/room-id
          :hx-trigger "click"
          :href url} (cheshire/generate-string item {:pretty true})]]))

(defn voting [ctx options]
  [:<>
   [:h2 "voting"]
   [:ul
    (map  #(voting-item ctx %) options)]])

(defn room-create-form [{:keys [path]}]
  (biff/form
   {:src path :method "post" :hx-post true}
   [:input {:type "text" :name "title"}]
   [:button {:type "submit"} "Create new room"]))

(defn room-list-item [{:keys [reitit.core/router]} room]
  (let [path (:path
              (r/match-by-name router paths-ids/room {:room-id (:xt/id room)}))]
    [:li
     [:a {:href path :hx-get path :hx-target ids/root-id :hx-push-url "true"} (cheshire/generate-string room {:pretty true})]]))

(defn room-list [room-list ctx]
  (ui/page {:title "room-list"}
           (room-create-form {:path "path"})
           [:ul (map (partial room-list-item ctx) room-list)]))

(defn room [{:keys [votes options room ctx]}]
  (let [{:keys [room/title closed?]} room]
    [:div {:id ids/room}
     [:h1
      title]
     (biff/form
      {:src "path" :method "post" :hx-post true}
      [:input {:type "text" :name "title"}]
      [:button "Submit"])

     [:h3 (if closed? true false)]
     [:p (random-uuid)]
     (voting ctx options)
     (results votes)]))

(defn room-page [ctx]
  (ui/page
   {:title "room page"}
   (room ctx)))
