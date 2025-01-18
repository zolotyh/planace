(ns com.zolotyh.planace.poker.ui
  (:require
   [cheshire.core :as cheshire]
   [com.biffweb :as biff]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]
   [com.zolotyh.planace.poker.ui.cards :as cards]
   [com.zolotyh.planace.poker.utils.url :as url-utils]
   [com.zolotyh.planace.poker.voting :as voting]
   [com.zolotyh.planace.ui :as ui]
   [reitit.core :as r]))

(defn results-item [item]
  [:li
   [:a {:href "test" :hx-post "test" :hx-trigger "click"}
    (cheshire/generate-string  item {:pretty true})]])

(defn results [votes room]
  (if (get-in room [:room/active-vote :vote/closed?])
    [:<>
     [:h2 "open"]
     [:ul (map results-item votes)]]
    [:<>
     [:h2 "closed"]]))

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
    [:div {:id ids/room :hx-swap "morph"}
     [:pre (cheshire/generate-string room)]
     [:h1
      title]
     (biff/form
      {:src "path" :method "post" :hx-post true}
      [:input {:type "text" :name "title" :class "input"}]
      [:button {:class "button"} "Submit"])

     [:button {:class (str "button " (if closed? "is-primary" ""))
               :hx-post toggle-path
               :hx-trigger :click
               :hx-target ids/room-id} (if closed? "Close" "Open")]

     [:p (random-uuid)]

     (cards/voting ctx sequences)
     (results votes room)]))

(defn with-ws-connection [elem url]
  [:div {:ws-connect url
         :hx-ext "ws" :hx-swap "morph"}
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
