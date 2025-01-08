(ns com.zolotyh.planace.poker
  (:require
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.poker.ctrls :as ctrls]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]))

(def module {:routes ["/poker" {:middleware [mid/wrap-signed-in]}
                      ["" {:get ctrls/room-list
                           :post ctrls/create-room}]
                      ["/room/:room-id" {:get ctrls/room :name paths-ids/room}]]})
