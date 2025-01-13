(ns com.zolotyh.planace.schema
  (:require
   [com.zolotyh.planace.poker.sequences :as sequences]))

(def Vote
  [:map {:closed true}
   [:xt/id           :vote/id]
   [:vote/room       :room/id]
   [:vote/closed?    :boolean]
   [:vote/created-at inst?]
   [:vote/sequence
    [:vector
     [:map
      [:key :string]
      [:val :int]]]]])

(defn vote [room-id]
  {:xt/id (random-uuid)
   :vote/room room-id
   :vote/closed? true
   :vote/sequence (sequences/natural)
   :vote/created-at :db/now})

(def schema
  {:user/id :uuid
   :user [:map {:closed true}
          [:xt/id                     :user/id]
          [:user/email                :string]
          [:user/joined-at            inst?]
          [:user/rooms      {:optional true}         [:vector :room/id]]]

   :room/id :uuid
   :room [:map {:closed true}
          [:xt/id       :room/id]
          [:room/owner    :user/id]
          [:room/title    :string]
          [:room/active-vote Vote]
          [:room/created-at inst?]]

   :vote/id :uuid
   :vote Vote

   :msg/id :uuid
   :msg [:map {:closed true}
         [:xt/id       :msg/id]
         [:msg/user    :user/id]
         [:msg/text    :string]
         [:msg/sent-at inst?]]})

(def module
  {:schema schema})
