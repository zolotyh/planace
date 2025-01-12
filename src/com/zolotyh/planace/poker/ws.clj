(ns com.zolotyh.planace.poker.ws)

(defn add [room-connections room-id conn]
  (update room-connections room-id #(conj (if % % (hash-set)) conn)))

(defn del [room-connections room-id conn]
  (update room-connections room-id #(disj % conn)))

(defn handler [{:keys [com.zolotyh.planace/room-connnections path-params]}]
  (let [room-id (:room-id path-params)]
    {:status 101
     :headers {"upgrade" "websocket"
               "connection" "upgrade"}
     :ws {:on-connect (fn [ws] (swap! room-connnections add room-id ws))
          :on-close   (fn [ws] (swap! room-connnections del room-id ws))}}))
