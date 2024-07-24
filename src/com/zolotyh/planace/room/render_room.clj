(ns com.zolotyh.planace.room.render-room
  (:require
   [com.zolotyh.planace.ui.page :refer [page]]))

(defn render-room [_] (page {:base/title "test"}
                            [:div "render-room"]))

