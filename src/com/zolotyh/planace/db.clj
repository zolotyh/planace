(ns com.zolotyh.planace.db
  (:require
   [com.biffweb :as biff]
   [com.zolotyh.planace.model :refer [create-new-vote]]
   [xtdb.api :as xt]))


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


(defn update-room [ctx room]
  (biff/submit-tx ctx [(merge room {:db/doc-type :room :db/op :update})]))

(defn create-room [{:keys [session biff/db] :as ctx} room-data]
  (let [user (xt/entity db (:uid session))
        user-id (:uid session)
        vote-id (random-uuid)
        room-id (random-uuid)
        room-data (merge {:room/current-vote vote-id
                          :room/owner user-id
                          :room/members [user-id]
                          :xt/id room-id}
                         default-room-data
                         room-data)
        vote-data (create-new-vote room-data (merge default-vote-data {:xt/id vote-id}))
        rooms (:user/rooms user)]

    (biff/submit-tx ctx [(merge {:db/doc-type :room} room-data)
                         (merge {:db/doc-type :vote} vote-data)
                         (merge user {:db/doc-type :user,
                                      :db/op :update,
                                      :user/rooms (if (vector? rooms)  (conj rooms room-id) [room-id])})])
    {:uuid room-id}))
