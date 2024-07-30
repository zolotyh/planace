(ns com.zolotyh.planace.room
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.ui :as ui]
   [ring.adapter.jetty9 :as jetty]
   [rum.core :as rum]))

(defn room [{:keys [path router reitit.core/match com.zolotyh.planace/votes] :as ctx}]
  (ui/page ctx
           [:div {:hx-ext "ws" :ws-connect "/room/ws/1"} (str (:path match))
            [:#messages]]))

(defn send-message [{:keys [session com.zolotyh.planace/votes] :as ctx} {:keys [text, room-id uid] :as msg}]
  (let [{:keys [id value]} (cheshire/parse-string text true)]
    (println (str text room-id uid id value))))

;; (defn notify-clients [{:keys [com.zolotyh.planace/chat-clients]} tx]
;;   (doseq [[op & args] (::xt/tx-ops tx)
;;           :when (= op ::xt/put)
;;           :let [[doc] args]
;;           :when (contains? doc :msg/text)
;;           :let [html (rum/render-static-markup
;;                       [:div#messages {:hx-swap-oob "afterbegin"}
;;                        (message doc)])]
;;           ws @chat-clients]
;;     (jetty/send! ws html)))
;;

(defn ws-vote-handler [{:keys [com.zolotyh.planace/votes path-params session] :as ctx}]
  (let [room-id (:room-id path-params)
        uid (:uid session)]
    {:status 101
     :headers {"upgrade" "websocket"
               "connection" "upgrade"}
     :ws {:on-connect (fn [ws]
                        (swap! votes update-in [room-id] conj ws)
                        (let [html (rum/render-static-markup [:#messages
                                                              {:hx-swap-oob "afterbegin"}
                                                              "hello"])]
                          (doseq [w (get-in @votes [room-id])]
                            (jetty/send! w html))))
          :on-text (fn [ws text-message]
                     (send-message ctx {:ws ws :text text-message :room-id room-id :uid session}))
          :on-close (fn [ws status-code reason]
                      (swap! votes update-in [room-id] dissoc session))}}))

(def module
  {:routes [["/room" {:middleware []}
             ["/ws/:room-id"                  {:get ws-vote-handler}]
             ["/:room-id/vote/:vote-id"                  {:get room}]]]})
