(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.biffweb :as biff]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]
   [com.zolotyh.planace.poker.utils.url :as url-utils]
   [com.zolotyh.planace.poker.voting :as voting]
   [com.zolotyh.planace.ui :as ui]
   [reitit.core :as r]))

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
    [:li
     [:a {:hx-post url
          :hx-target ids/room-id
          :hx-vals (cheshire/generate-string {:val (:val item) :key (:key item)})
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
     [:a {:href path :hx-get path :hx-target ids/root-id :hx-push-url "true"} (:room/title room)]]))

(defn room-list [room-list ctx]
  (ui/page {:title "room-list"}
           (room-create-form {:path "path"})
           [:ul (map (partial room-list-item ctx) room-list)]))

(defn room [{:keys [room ctx]}]
  (let [{:keys [room/title room/active-vote]} room
        {:keys [vote/closed?]} active-vote
        {:keys [reitit.core/router session]} ctx
        user-id (:uid session)
        votes (get-in room [:room/active-vote :vote/results])
        active-vote (voting/vote-by-user-id votes user-id)
        sequences (voting/update-sequence-by-active-vote
                   (get-in room [:room/active-vote :vote/sequence])
                   active-vote)
        toggle-match (r/match-by-name router paths-ids/toggle {:room-id (:xt/id room)})
        toggle-path (:path toggle-match)]
    [:div {:id ids/room}
     [:pre (cheshire/generate-string room)]
     [:h1
      title]
     (biff/form
      {:src "path" :method "post" :hx-post true}
      [:input {:type "text" :name "title"}]
      [:button "Submit"])

     [:h3 {:hx-post toggle-path :hx-trigger :click :hx-target ids/room-id} (if closed? true false)]
     [:p (random-uuid)]

     [:pre
      (cheshire/generate-string sequences)]

     (voting ctx sequences)
     (results votes)]))

(defn with-ws-connection [elem url]
  [:div {:ws-connect url
         :hx-ext "ws"}
   elem])

(defn room-page [{:keys [ctx] :as params}]
  (let [{:keys [path-params reitit.core/router]} ctx
        room-id (:room-id path-params)
        url (url-utils/generate router paths-ids/ws {:room-id room-id})]
    (ui/page
     {:title "room page"}
     (with-ws-connection
       (room params)
       url))))
