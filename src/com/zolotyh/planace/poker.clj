(ns com.zolotyh.planace.poker
  (:require
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.poker.ctrls :as ctrls]
   [com.zolotyh.planace.poker.ws :as ws]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]))

(def module {:routes ["/poker" {:middleware [mid/wrap-signed-in]}
                      ["" {:name paths-ids/room-list
                           :get ctrls/room-list
                           :post ctrls/create-room}]
                      ["/room/:room-id"
                       ["" {:name paths-ids/room
                            :get ctrls/room
                            :put ctrls/update-room}]
                       ["/vote"   {:name paths-ids/vote
                                   :post ctrls/vote}]
                       ["/toggle" {:name paths-ids/toggle
                                   :post ctrls/room-toggle}]
                       ["/ws" {:name paths-ids/ws
                               :get ws/handler}]]]})


