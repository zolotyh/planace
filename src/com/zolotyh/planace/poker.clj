(ns com.zolotyh.planace.poker
  (:require
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.poker.ctrls :as ctrls]))

(def module {:routes ["/poker" {:middleware [mid/wrap-signed-in]}
                      ["" {:get ctrls/room}]]})
