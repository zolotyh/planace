(ns com.zolotyh.planace.generate
  (:require [malli.provider :as mp]
            [malli.generator :as generate]))

(def samples
  {:vote-list {:id12312 {:title "Hello", :voters [{:user "1", :vote 1}]}}})

;; {
;;  "vote-list": {
;;                "id12312": {
;;                            "title": "Hello",
;;                            "voters": [
;;                                       {
;;                                       "user": "1",
;;                                       "vote": 1}]}}}
;;
;;

(def schema
  [:vector
   [:map-of :keyword
    [:map
     [:title :string]
     [:voters [:vector [:map [:user :string] [:vote :int]]]]]]])

(generate/generate schema)

(mp/provide samples)
