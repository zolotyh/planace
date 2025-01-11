(ns com.zolotyh.planace.poker.ctrls
  (:require
   [cheshire.core :as cheshire]
   [clojure.tools.logging :as log]
   [com.zolotyh.planace.poker.db :as db]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]
   [com.zolotyh.planace.poker.ui :as ui]
   [com.zolotyh.planace.poker.utils.http :as http-utils]
   [reitit.core :as r]
   [xtdb.api :as xt]))

(def test-data {:votes
                (->>
                 (range 2)
                 (map #(hash-map :val % :user (random-uuid))))
                :options
                (->>
                 (range 3)
                 (map #(hash-map :val % :keys %)))
                :room {:room/title "<>room title<> "
                       :closed? true}})

(defn room [{:keys [biff/db path-params] :as ctx}]
  (let [room-id (:room-id path-params)
        room (xt/entity db (parse-uuid room-id))]
    (ui/room-page (merge test-data {:room room, :ctx ctx}))))

(def swapStr "innerHTML swap:0.1s settle:0.3s transition:true")

(defn create-room [{:keys [session params reitit.core/router biff/db] :as ctx}]
  (let [title (:title params)
        owner (xt/entity db (:uid session))
        room (db/create-room ctx title owner)
        resp-status (http-utils/htmx-status ctx)
        redirect-url (:path
                      (r/match-by-name router paths-ids/room {:room-id (:xt/id room)}))]

    {:status resp-status
     :headers {"HX-Location" (cheshire/generate-string
                              {:path redirect-url
                               :swap swapStr
                               :target ids/root-id})
               "location" redirect-url}}))

(defn room-list [{:keys [session biff/db] :as ctx}]
  (let [uid (:uid session)
        user (xt/entity db uid)
        rooms (db/q-by-ids ctx (:user/rooms user))]
    (ui/room-list rooms ctx)))

(defn update-room [_]
  [:div "update room"])

(defn vote [{:keys [session path-params biff/db] :as ctx}]
  (let [room-id (parse-uuid
                 (:room-id path-params))
        room (xt/entity db room-id)]
    (ui/room (merge test-data {:room room, :ctx ctx}))))

