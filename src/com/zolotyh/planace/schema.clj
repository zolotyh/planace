(ns com.zolotyh.planace.schema
  (:require
   [malli.generator :as mg]))

(def schema
  {:user/id :uuid
   :user [:map {:closed true}
          [:xt/id                     :user/id]
          [:user/email                :string]
          [:user/last-nane            :string]
          [:user/first-name           :string]
          [:user/joined-at  inst?]]

   :room/id :uuid
   :room [:map {:closed true}
          [:xt/id :uuid]
          [:room/title :string]
          [:room/current-vote :uuid]
          [:room/owner :uuid]
          ; ; [:room/default-vote-type :vote/vote-type]
          [:room/members [:vector :uuid]]]

   :vote/vote-type [:enum :fib :natural :t-shirts]
   :vote/id :uuid
   :vote [:map {:closed true}
          [:xt/id :vote/id]
          [:vote/room :room/id]
          [:vote/open :boolean]
          ; [:vote/type :vote/vote-type]
          [:vote/result [:vector
                         [:map {:closed true}
                          [:vote :int]
                          [:user :user/id]]]]
          [:vote/title :string]]})
(mg/generate (:room schema))

(def module
  {:schema schema})
