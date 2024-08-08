(ns com.zolotyh.planace.app
  (:require
   [com.biffweb :as biff]
   [com.zolotyh.planace.db :refer [q-by-ids]]
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.ui :as ui]
   [xtdb.api :as xt]))

(defn bar-form []
  (biff/form
   {:hx-post "/app/create-room" :class "col-span-12 rounded-sm border border-stroke bg-white px-5 pb-5 pt-7.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:col-span-8 text-slate-900"}
   [:label.block {:for "bar"} "Room name"]
   [:.h-1]
   [:.flex [:input.w-full#bar {:type "text", :name "bar", :value ""}]
    [:.w-3] [:button.btn {:type "submit" :class "w-full cursor-pointer rounded-lg border border-primary bg-primary p-4 font-medium text-white transition hover:bg-opacity-90"} "Update"]]
   [:.h-1]
   [:.text-sm.text-gray-600 "This demonstrates updating a value with HTMX."]))


(defn room [room]
  [:li (:room/title room)])

(defn room-list [rooms]
  [:<>
   [:h1.text-xl "Room list"]
   [:ui (map room rooms)]])

(defn main-page [{:keys [session biff/db] :as ctx}]
  (let [uid (:uid session)
        user (xt/entity db uid)
        rooms (q-by-ids ctx (:user/rooms user))]
    (ui/page ctx (ui/center {:class "h-screen bg-green text-white flex-col"}
                            [:<>
                             (room-list rooms)
                             (bar-form)
                             [:div "lorem lorem lorem 2"]]))))


(defn app [{:keys [i18n] :as ctx}]
  (ui/page ctx
           [:<>
            (ui/main-layout
             {:header "header"
              :footer "footer"
              :right-sidebar "right-sidebar"
              :profile "profile"
              :main "main"})]))
            ; [:div (i18n [:page/title])]
            ; (ui/vote-results)
            ; (ui/vote-panel)]))

(def module {:routes ["/app" {:middleware [mid/wrap-signed-in mid/i18n]}
                      ["" {:get main-page}]
                      ["/room/:room-id" {:get app}]]})
