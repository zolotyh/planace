(ns com.zolotyh.planace.poker.ws)

(defn add-connection [room-connections room-id conn]
  (update room-connections room-id #(conj (if % % (hash-set)) conn)))

(defn remove-connection [room-connections room-id conn]
  (update room-connections room-id #(disj % conn)))

(defn handler [{:keys [com.zolotyh.planace/room-connections  path-params]}]
  (let [room-id (:room-id path-params)]
    {:status 101
     :headers {"upgrade" "websocket"
               "connection" "upgrade"}
     :ws {:on-connect (fn [ws] (swap! room-connections add-connection room-id ws))
          :on-close   (fn [ws & _rest] (swap! room-connections remove-connection room-id ws))}}))
