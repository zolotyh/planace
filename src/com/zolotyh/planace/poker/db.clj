(ns com.zolotyh.planace.poker.db
  (:require
   [com.biffweb :as biff]))

(defn q-by-ids [{:keys [biff/db]} ids]
  (let [query '{:find (pull item [*])
                :in [[id ...]]
                :where [[item :xt/id id]]}]
    (biff/q db query ids)))

(defn create-room [ctx title user]
  (let [rooms (:user/rooms user)
        id (:xt/id user)
        room-id (random-uuid)
        room {:xt/id room-id
              :room/title title
              :room/created-at :db/now
              :room/owner id}
        new-rooms (if (vector? rooms)  (conj rooms room-id) [room-id])]

    (biff/submit-tx ctx
                    [(merge room {:db/op :create :db/doc-type :room})
                     (merge user {:db/doc-type :user,
                                  :db/op :update,
                                  :user/rooms new-rooms})])
    room))
