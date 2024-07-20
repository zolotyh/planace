(ns com.zolotyh.planace.schema)

(def schema
  {:user/id :uuid
   :user [:map {:closed true}
          [:xt/id                     :user/id]
          [:user/email                :string]
          [:user/joined-at            inst?]
          [:user/first-name {:optional true} :string]
          [:user/last-name  {:optional true} :string]
          [:user/foo {:optional true} :string]
          [:user/bar {:optional true} :string]]

   :room/id :uuid
   :room [:map {:closed true}
          [:xt/id       :room/id]
          [:room/user    :user/id]
          [:room/name    :string]
          ;; [:room/items [:map {:closed true}
          ;;               [:voters [:map {:closed true}
          ;;                         [:user :user/id]
          ;;                         [:vote int]]]]]
          [:room/created-at inst?]]

   :msg/id :uuid
   :msg [:map {:closed true}
         [:xt/id       :msg/id]
         [:msg/user    :user/id]
         [:msg/text    :string]
         [:msg/sent-at inst?]]})

(def module
  {:schema schema})
