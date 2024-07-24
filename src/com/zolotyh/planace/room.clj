(ns com.zolotyh.planace.room
  (:require [com.zolotyh.planace.middleware :as mid]
            [com.zolotyh.planace.room.render-room :as render-room]))

(def module
  {:routes ["/room" {:middleware [mid/wrap-signed-in]}
            ["/:room-id" {:get render-room/render-room}]]})
