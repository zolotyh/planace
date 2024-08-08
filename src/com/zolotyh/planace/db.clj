(ns com.zolotyh.planace.db
  (:require
   [com.biffweb :as biff]
   [com.zolotyh.planace.model :refer [create-new-vote]]))


(defn q-by-ids [{:keys [biff/db]} ids]
  (let [query '{:find (pull item [*])
                :in [[id ...]]
                :where [[item :xt/id id]]}]
    (biff/q db query ids)))

(def default-room-data {:room/default-vote-type :natural
                        :room/members []})

(def default-vote-data {:vote/open false
                        :vote/title "New Vote"
                        :vote/type (:room/default-vote-type default-room-data)})

(defn create-room [{:keys [session] :as ctx} room-data]
  (let [current-user-uid (:uid session)
        vote-id (random-uuid)
        room-id (random-uuid)
        room-data (merge {:db/doc-type :room
                          :room/current-vote vote-id
                          :room/owner current-user-uid
                          :room/members [current-user-uid]
                          :xt/id room-id}
                         default-room-data
                         room-data)
        vote-data (create-new-vote room-data (merge default-vote-data {:xt/id vote-id}))]
    (biff/submit-tx ctx [room-data])
    (biff/submit-tx ctx [(merge {:db/doc-type :vote} vote-data)])))
