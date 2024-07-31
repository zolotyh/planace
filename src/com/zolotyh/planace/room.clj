(ns com.zolotyh.planace.room
  (:require [com.biffweb :as biff]
            [com.zolotyh.planace.ui :as ui]
            [xtdb.api :as xt]))


;;             [xtdb.api :as xt]))
;;
;; (def vote-result-id "vote-result")
;;
;; (defn vote-button
;;   [[key value]]
;;   [:button.px-5.py-3
;;    {:hx-post "/room/1",
;;     :hx-vals (cheshire/encode {:value value}),
;;     :hx-trigger "click",
;;     :hx-target (str "#" vote-result-id)} key])
;;
;; (defn vote-list [] [:div {:id vote-result-id} "vote-list"])
;;
;; (defn vote-panel
;;   []
;;   [:div
;;    (->> (:fib sequences)
;;         (map vote-button))])
;;
;; (defn vote-toogle
;;   [room]
;;   [:buton.bg-slate-900.text-white-100.px-5.py-3.rounded
;;    {:hx-trigger "click",
;;     :hx-target (str "#" vote-result-id),
;;     :hx-post (str "/room/" (:xt/id room) "/toggle")} (str (:room/name
;;     room))])
;;
;;
;; (defn room
;;   [{:keys [path router reitit.core/match com.zolotyh.planace/votes biff/db
;;            path-params],
;;     :as ctx}]
;;   (let [room-id (:room-id path-params)
;;         room (xt/entity db (parse-uuid room-id))
;;         vote (xt/entity db (:room/active-vote room))]
;;     (ui/page ctx
;;              [:div {:hx-ext "ws", :ws-connect "/room/ws/1"} (str (:path
;;              match))
;;               [:#messages] (boolean (:open? vote)) (vote-toogle room)
;;               (vote-list) (vote-panel)])))
;;
;; (defn send-message
;;   [{:keys [session com.zolotyh.planace/votes], :as ctx}
;;    {:keys [text room-id uid], :as msg}]
;;   (let [{:keys [id value]} (cheshire/parse-string text true)]
;;     (println (str text room-id uid id value))))
;;
;; ;; (defn notify-clients [{:keys [com.zolotyh.planace/chat-clients]} tx]
;; ;;   (doseq [[op & args] (::xt/tx-ops tx)
;; ;;           :when (= op ::xt/put)
;; ;;           :let [[doc] args]
;; ;;           :when (contains? doc :msg/text)
;; ;;           :let [html (rum/render-static-markup
;; ;;                       [:div#messages {:hx-swap-oob "afterbegin"}
;; ;;                        (message doc)])]
;; ;;           ws @chat-clients]
;; ;;     (jetty/send! ws html)))
;; ;;
;;
;; (defn ws-vote-handler
;;   [{:keys [com.zolotyh.planace/votes path-params session], :as ctx}]
;;   (let [room-id (:room-id path-params)
;;         uid (:uid session)]
;;     {:status 101,
;;      :headers {"upgrade" "websocket", "connection" "upgrade"},
;;      :ws {:on-connect
;;             (fn [ws]
;;               (swap! votes update-in [room-id] conj ws)
;;               (let [html (rum/render-static-markup
;;                            [:#messages {:hx-swap-oob "afterbegin"}
;;                            "hello"])]
;;                 (doseq [w (get-in @votes [room-id])] (jetty/send! w
;;                 html)))),
;;           :on-text
;;             (fn [ws text-message]
;;               (send-message
;;                 ctx
;;                 {:ws ws, :text text-message, :room-id room-id, :uid
;;                 session})),
;;           :on-close (fn [ws status-code reason]
;;                       (swap! votes update-in [room-id] dissoc
  ;;                       session))}}))
;;
;; (defn- vote [_] [:div "vote"])
;;
;;
;;
;;
;; (defn get-room
;;   [{:keys [path-params biff/db]}]
;;   (let [room (xt/entity db (parse-uuid (path-params :room-id)))]
;;     (merge room {:active-vote (xt/entity db (:room/active-vote room))})))
;;
;; (defn render-vote
;;   [{:keys [vote/open?], :as vote}]
;;   [:div {:id vote-result-id} open?])
;;
;; (defn toogle-vote
;;   [{:keys [path-params biff/db], :as ctx}]
;;   (let [room (xt/entity db (parse-uuid (path-params :room-id)))
;;         vote (xt/entity db (:room/active-vote room))
;;         new-vote (xt/submit-tx db
;;                                [[:xtdb.api/put
;;                                  (assoc vote
;;                                    :vote/open (not (:vote/open vote)))]])]
;;     (render-vote new-vote)))
;;
;;
;; (def module
;;   {:routes [["/room" {:middleware []} ["/ws/:room-id" {:get
;;   ws-vote-handler}]
;;              ;; ["/:room-id/vote/:vote-id" {:post vote}]
;;              ["/:room-id" {:get room, :post render-vote}]
;;              ["/:room-id/vote/:vote-id/toggle" {:post toogle-vote}]]]})
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


(def module
  {:routes ["/room" {:middleware []}
            ["/vote/:vote-id/toggle" {:post toggle-vote}]
            ["/view/:room-id" {:get room}]]})
