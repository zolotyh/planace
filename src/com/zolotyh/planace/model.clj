(ns com.zolotyh.planace.model
  (:require
   [com.rpl.specter :as s]))

(defn add-user-vote [old-vote user-id value]
  (s/setval
   [:vote/result
    #_{:clj-kondo/ignore [:unresolved-var]}
    s/ALL
    (fn [v] (= user-id (:user v)))] {:user user-id :vote value} old-vote))

(defn create-new-vote [room vote]
  (merge
   vote
   {:vote/room (:xt/id room)
    :vote/result
    (reduce
     #(conj %1 {:user %2})
     []
     (:room/members room))}))

