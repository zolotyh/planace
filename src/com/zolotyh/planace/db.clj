(ns com.zolotyh.planace.db
  (:require
   [com.zolotyh.planace.model :refer [create-new-vote]]
   [xtdb.api :as xt]))

(def default-room-data {:room/default-vote-type :natural})

(def default-vote-data {:vote/open false
                        :vote/title "New Vote"
                        :vote/type (:room/default-vote-type default-room-data)})



(defn create-room [ctx room-data] {}
  (let [room (xt/submit-tx
              ctx
              (merge {:db/op :create
                      :db/doc-type :room}
                     default-room-data
                     room-data))

        vote-data (merge {:db/op :create
                          :db/doc-type :create
                          :vote/room (:xt/id room)}
                         default-vote-data)

        vote (xt/submit-tx
              ctx
              (merge
               vote-data
               (create-new-vote room vote-data)))]

    (xt/submit-tx ctx (merge {:db/op :update
                              :db/doc-type :room
                              :room/active-vote (:xt/id vote)}))))

