(ns com.zolotyh.planace.room
  (:require [com.biffweb :as biff]
            [com.zolotyh.planace.ui :as ui]
            [ring.adapter.jetty9 :as jetty]
            [rum.core :as rum]
            [xtdb.api :as xt]))


(defn ws-vote-handler
  [{:keys [com.zolotyh.planace/votes path-params session]}]
  (let [room-id (:room-id path-params)]
    {:status 101,
     :headers {"upgrade" "websocket", "connection" "upgrade"},
     :ws {:on-connect (fn [ws] (swap! votes update-in [room-id] conj ws)),
          :on-close (fn [ws] (swap! votes update-in [room-id] dissoc ws))}}))

(defn toggle-vote-button
  [vote]
  [:button
   {:hx-trigger "click",
    :hx-target (str "#vote"),
    :hx-post (str "/room/vote/" (:xt/id vote) "/toggle")} "toggle"])

(defn render-vote
  [vote]
  [:div {:id "vote"} (:vote/open? vote) (toggle-vote-button vote)])


(defn toggle-vote
  [{:keys [biff/db path-params], :as ctx}]
  (let [vote (xt/entity db (parse-uuid (:vote-id path-params)))
        new-vote (update vote :vote/open? not)]
    (biff/submit-tx ctx [(merge {:db/op :update, :db/doc-type :vote} new-vote)])
    (render-vote new-vote)))

(defn render-room
  [{:keys [room vote], :as ctx}]
  [:div {:id "room"} (:room/name room) (render-vote vote)])

(defn room
  [{:keys [path-params biff/db], :as ctx}]
  (let [room (xt/entity db (parse-uuid (path-params :room-id)))
        vote (xt/entity db (:room/active-vote room))]
    (ui/page ctx (render-room {:vote vote, :room room}))))

(defn notify-room-connections
  [{:keys [com.zolotyh.planace/votes]} tx]
  (doseq [[op & args] (::xt/tx-ops tx)
          :when (= op ::xt/put)
          :let [[vote] args]
          :when (contains? vote :vote/title)
          :let [html (rum/render-static-markup (render-vote vote))]
          ws (get-in @votes [(:vote/room vote)])]
    (jetty/send! ws html)))



(def module
  {:routes
     ["/room" {:middleware []} ["/vote/:vote-id/toggle" {:post toggle-vote}]
      ["/view/:room-id" {:get room}] ["/ws/:room-id" {:get ws-vote-handler}]],
   :on-tx notify-room-connections})
