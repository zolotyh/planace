(ns com.zolotyh.planace.room
  (:require
   [cheshire.core :as cheshire]
   [com.biffweb :as biff]
   [com.zolotyh.planace.ui :as ui]
   [com.zolotyh.planace.utils.sequences :refer
    [get-vote-result sequences]]
   [ring.adapter.jetty9 :as jetty]
   [rum.core :as rum]
   [xtdb.api :as xt]))

(defn get-results [result] (reduce (fn [a v] (conj a (:vote v))) [] result))

(defn vote-switcher-item [item]
  [:button {:hx-post (str "/room/edit/" (:vote/room item))
            :hx-trigger "click"
            :hx-target "#room"
            :hx-vals (cheshire/generate-string {:room/active-vote (:xt/id item)})} (:vote/title item)])

(defn vote-switcher [vote-list]
  [:div (map vote-switcher-item vote-list)])

(defn ws-vote-handler
  [{:keys [com.zolotyh.planace/votes path-params]}]
  (let [room-id (:room-id path-params)]
    {:status 101,
     :headers {"upgrade" "websocket", "connection" "upgrade"},
     :ws {:on-connect (fn [ws] (swap! votes update-in [room-id] conj ws)),
          :on-close (fn [ws] (swap! votes update-in [room-id] dissoc ws))}}))

(defn toggle-vote-button
  [vote]
  [:button.bg-slate-900.text-slate-200.px-5.py-2.mx-2.rounded
   {:hx-trigger "click",
    :hx-target (str "#vote"),
    :hx-post (str "/room/vote/" (:xt/id vote) "/toggle")}
   (if (:vote/open? vote) "Закрыть" "Открыть")])

(defn card
  [result]
  [:table [:tr [:th "Имя"] [:td (:first-name result)]]
   [:tr [:th "Фамилия"] [:td (:last-name result)]]
   [:tr [:th "Значение"] [:td (:vote result)]]])

(defn closed-card [_] [:div "Закрыто"])

(defn results [vote] (map card (vals (:vote/results vote))))
(defn closed-results [vote] (map closed-card (vals (:vote/results vote))))

(defn render-vote
  [vote]
  [:div {:id "vote"}
   (if (:vote/open? vote)
     [:<>
      [:div.text-3xl "Итоговый результат: "
       (:key (get-vote-result (get-results (vals (:vote/results vote)))
                              :median
                              :fib))] (results vote)]
     (closed-results vote)) (toggle-vote-button vote)])

(defn vote-item
  [{:keys [vote/room xt/id]} [key value]]
  [:button.px-5.py-3.mx-2.bg-slate-900
   {:hx-post (str "/room/vote/" id), :hx-trigger "click" :hx-target "#vote" :hx-vals (cheshire/generate-string {:vote value})} key])

(defn vote-panel
  [vote]
  (let [vote-type (:vote/type vote)]
    [:<> [:h1.size-16 "lorem"] [:div (str "voting type: " (:vote/type vote))]
     (map (partial vote-item vote) (vote-type sequences))]))

(defn toggle-vote
  [{:keys [biff/db path-params], :as ctx}]
  (let [vote (xt/entity db (parse-uuid (:vote-id path-params)))
        new-vote (update vote :vote/open? not)]
    (biff/submit-tx ctx [(merge {:db/op :update, :db/doc-type :vote} new-vote)])
    (render-vote new-vote)))

(defn render-room
  [{:keys [room vote vote-list ctx]}]
  [:div {:id "room", :hx-ext "ws", :ws-connect (str "/room/ws/" (:xt/id room))}
   (:room/name room)
   [:div (str "active-vote" (:vote/title vote))]
   (render-vote vote)
   (vote-panel vote)
   (vote-switcher vote-list)])

(defn update-room
  [{:keys [path-params biff/db params], :as ctx}]
  (let [room-uuid (parse-uuid (:room-id path-params))
        room (xt/entity db room-uuid)
        vote (xt/entity db (:room/active-vote room))
        vote-list (biff/lookup-all db :vote/room room-uuid)
        new-room (merge room {:room/active-vote (parse-uuid (get params "room/active-vote"))})]
    (biff/submit-tx ctx [(merge {:db/op :update, :db/doc-type :room} new-room)])
    (render-room {:vote vote, :room new-room, :vote-list vote-list :ctx ctx})))

(defn room
  [{:keys [path-params biff/db], :as ctx}]
  (let [room-uuid (parse-uuid (:room-id path-params))
        room (xt/entity db room-uuid)
        vote (xt/entity db (:room/active-vote room))
        vote-list (biff/lookup-all db :vote/room room-uuid)]
    (ui/page ctx (render-room {:vote vote, :room room, :vote-list vote-list :ctx ctx}))))

(defn notify-room-connections
  [{:keys [com.zolotyh.planace/votes]} tx]
  (doseq [[op & args] (::xt/tx-ops tx)
          :when (= op ::xt/put)
          :let [[vote] args]
          :when (or
                 (contains? vote :vote/title)
                 (contains? vote :room/name))

          :let [html (rum/render-static-markup (render-vote vote))]
          ws (get-in @votes [(str (:vote/room vote))])]
    (jetty/send! ws html)))

(defn vote
  [{:keys [biff/db path-params session params], :as ctx}]
  (let [vote (xt/entity db (parse-uuid (:vote-id path-params)))
        uuid (:uid session)
        user (xt/entity db (:uid session))
        new-vote (assoc-in vote [:vote/results uuid]
                           {:vote (parse-long (params :vote))
                            :first-name (:user/first-name user)
                            :last-name (:user/last-name user)})]
    (biff/submit-tx ctx [(merge {:db/op :update, :db/doc-type :vote} new-vote)])
    (render-vote new-vote)))

(def module
  {:routes ["/room" {:middleware []}
            ["/vote/:vote-id/toggle" {:post toggle-vote}]
            ["/view/:room-id" {:get room}]
            ["/vote/:vote-id" {:post vote}]
            ["/edit/:room-id" {:post update-room}]
            ["/ws/:room-id" {:get ws-vote-handler}]],
   :on-tx notify-room-connections})
