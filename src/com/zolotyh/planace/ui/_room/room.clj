(ns com.zolotyh.planace.ui.room.room
  (:require [com.zolotyh.planace.ui :as ui]))

;; (defn render-room
;;   [{:keys [path-params biff/db]}]
;;   (let [room (xt/entity db (parse-uuid (:room-id path-params)))
;;         vote (xt/entity db (:room/active-vote room))]
;;     (ui/page {:base/title (str (:token path-params))}
;;              [:div [:main.main (main)] (footer)])))
;;              ;; (voter-list (vals (:vote/results vote)))
;;              ;; [:div "vote: " (:vote/title vote) "active vote id: "
;;              ;;  (:room/active-vote room)]
;;              ;; [:p "room: " (:room/name room)])))

(defn room [_] (ui/page {:base/title "Room"} [:div "room"]))

