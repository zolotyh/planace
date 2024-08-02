(ns com.zolotyh.planace.schema)

(def schema
  {:user/id :uuid,
   :user [:map {:closed true} [:xt/id :user/id] [:user/email :string]
          [:user/joined-at inst?] [:user/first-name {:optional true} :string]
          [:user/last-name {:optional true} :string]
          [:user/foo {:optional true} :string]
          [:user/bar {:optional true} :string]],
   :room/id :uuid,
   :room [:map {:closed true} [:xt/id :room/id] [:room/user :user/id]
          [:room/active-vote :vote/id] [:room/name :string]
          [:room/created-at inst?] [:room/update-at inst?]],
   :vote/id :uuid,
   :vote [:map {:closed false} [:xt/id :vote/id] [:vote/open? :boolean]
          [:vote/owner :user/id]
          [:vote/results
           [:map-of :user/id
            [:map {:closed true} [:vote int?]
             [:first-name {:optional true} :string]
             [:last-name {:optional true} :string]]]]
          [:vote/room :room/id]
          [:vote/type [:enum :fib :t-shirts :simple]] [:vote/title :string]
          [:vote/updated-at inst?]],
   :msg/id :uuid,
   :msg [:map {:closed true} [:xt/id :msg/id] [:msg/user :user/id]
         [:msg/text :string] [:msg/sent-at inst?]]})

(def module {:schema schema})
