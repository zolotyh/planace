(ns com.zolotyh.planace.poker.db
  (:require
   [com.biffweb :as biff]
   [com.zolotyh.planace.poker.ui :as ui]
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

(defn update-room [{:keys [biff/db path-params params]}]
  (let [room-id (parse-uuid (:room-id path-params))]
    (xt/submit-tx db [[::xt/put {:xt/id room-id :msg/title (:title params)}]])
    (ui/room-page (xt/entity db room-id))))

