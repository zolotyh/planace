(ns com.zolotyh.planace.schema)

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
          [:room/closed? {:optional true}    :boolean]
          [:room/created-at inst?]]

   :vote/id :uuid
   :vote [:map {:closed true}
          [:xt/id       :vote/id]
          [:vote/room    :room/id]
          [:vote/closed?    :room/id]
          [:vote/created-at inst?]]

   :msg/id :uuid
   :msg [:map {:closed true}
         [:xt/id       :msg/id]
         [:msg/user    :user/id]
         [:msg/text    :string]
         [:msg/sent-at inst?]]})

(def module
  {:schema schema})
