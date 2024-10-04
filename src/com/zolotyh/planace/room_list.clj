(ns com.zolotyh.planace.room-list
  (:require
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.ui :as ui]))


(def id "search")
(def spinner-id "spinner")
(def param "s")

(defn search [{:keys [query-params]}]
  [:div
   [:input {:value (query-params param)
            :type "text"
            :name "s"
            :hx-params "*"
            :hx-indicator (str "#" spinner-id)
            :hx-get "/room-list/search"
            :hx-trigger "input changed delay:500ms, search"
            :hx-target (str "#" id)
            :placeholder "Search"}]])



(defn room [_ctx, value]
  [:div (str "room" value)])

(defn room-list [ctx]
  (let [items (range (rand-int 10))]
    [:<>
     [:div {:id id}
      [:div {:id spinner-id :class "loader"}]
      (map (partial room ctx) items)]]))


(defn room-page [ctx]
  (ui/page
   {}
   [:div
    [:div]
    (search ctx)
    (room-list ctx)]))


(def module {:routes ["/room-list" {:middleware [mid/i18n]}
                      ["/search" {:get room-list}]
                      ["" {:get room-page}]]})
