(ns com.zolotyh.planace.poker.ui.cards
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.poker.path-ids :as paths-ids]
   [reitit.core :as r]))

(defn voting-item [{:keys [reitit.core/router path-params]} item]
  (let [room-id (:room-id path-params)
        url (:path
             (r/match-by-name router paths-ids/vote {:room-id room-id}))]
    [:li {:class
          (if (:active item)
            "transition duration-300 has-background-primary-25 has-text-primary-25-invert box aspect-[63/88] cursor-pointer max-w-23 text-4xl flex items-center justify-center font-extralight -translate-y-2 p-0"
            "transition duration-300 box aspect-[63/88] cursor-pointer max-w-23 text-4xl flex items-center justify-center font-extralight p-0")}
     [:a {:class "block w-full h-full flex items-center justify-center"
          :hx-post url
          :hx-target ids/room-id
          :hx-swap "morph"
          :hx-vals (cheshire/generate-string {:val (:val item) :key (:key item)})
          :hx-trigger "click"
          :href url} (:key item)]]))

(defn voting [ctx options]
  [:<>
   [:h2 "voting"]
   ; [:div {:class "grid 2xl:grid-cols-16 xl:grid-cols-16 md:grid-cols-12 sm:grid-cols-4 xs:grid-cols-1 gap-4 -translate-y-20 -mb-8"}]
   [:div {:class "grid 2xl:grid-cols-16 xl:grid-cols-16 md:grid-cols-12 sm:grid-cols-4 xs:grid-cols-1 gap-4"}
    (map  #(voting-item ctx %) options)]])
