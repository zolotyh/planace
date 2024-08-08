(ns com.zolotyh.planace.schema)

(def schema
  {:user/id :uuid
   :user [:map {:closed true}
          [:xt/id                     :user/id]
          [:user/email                :string]
          [:user/last-nane            string?]
          [:user/first-name           string?]
          [:user/rooms {:optional true} [:vector :room/id]]
          [:user/joined-at  inst?]]


   :room/id :uuid
   :room [:map {:closed true}
          [:xt/id :room/id]
          [:room/title :string]
          [:room/current-vote :vote/id]
          [:room/owner :user/id]
          [:room/default-vote-type :vote/vote-type]
          [:room/members [:vector :user/id]]]

   :vote/vote-type [:enum :fib :natural :t-shirts]
   :vote/id :uuid
   :vote [:map {:closed true}
          [:xt/id :vote/id]
          [:vote/room :room/id]
          [:vote/open :boolean]
          [:vote/type :vote/vote-type]
          [:vote/title :string]
          [:vote/result [:vector
                         [:map {:closed true}
                          [:vote {:optional true} int?]
                          [:user :user/id]]]]]})
(def module
  {:schema schema})
