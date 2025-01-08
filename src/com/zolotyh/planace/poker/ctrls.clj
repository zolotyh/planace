(ns com.zolotyh.planace.poker.ctrls
  (:require
   [cheshire.core :as cheshire]
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
                :room {:title "<>room title<> "
                       :closed? true}})

(defn room [_]
  (ui/room test-data))

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

(defn room-list [ctx]
  (ui/room-list ctx))
