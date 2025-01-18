(ns com.zolotyh.planace.poker.db
  (:require
   [com.biffweb :as biff]
   [com.zolotyh.planace.schema :as schema]
   [xtdb.api :as xt]))

(defn q-by-ids [{:keys [biff/db]} ids]
  (let [query '{:find (pull item [*])
                :in [[id ...]]
                :where [[item :xt/id id]]}]
    (biff/q db query ids)))

(defn create-room [ctx title user]
  (let [rooms (:user/rooms user)
        id (:xt/id user)
        room-id (random-uuid)
        vote-id (random-uuid)
        vote (schema/vote-defaults vote-id room-id)
        room (schema/room-defaults {:owner-id id
                                    :room-id room-id
                                    :vote vote
                                    :title title})
        new-rooms (if (vector? rooms)  (conj rooms room-id) [room-id])]

    (biff/submit-tx ctx
                    [(merge room {:db/op :create :db/doc-type :room})
                     (merge vote {:db/op :create :db/doc-type :vote})
                     (merge user {:db/doc-type :user,
                                  :db/op :update,
                                  :user/rooms new-rooms})])
    room))

(defn update-in-room [{:keys [path-params biff/db] :as ctx} path update-fn]
  (let [room-id (parse-uuid (:room-id path-params))
        room  (xt/entity db room-id)
        updated-room (update-in room path update-fn)
        updated-vote (:room/active-vote updated-room)]
    (biff/submit-tx ctx
                    [(merge
                      {:db/op :update
                       :db/doc-type :room}
                      updated-room)
                     (merge
                      {:db/op :update
                       :db/doc-type :vote}
                      updated-vote)])
    updated-room))
