(ns com.zolotyh.planace.app
  (:require
   [cheshire.core :refer [generate-string]]
   [com.biffweb :as biff]
   [com.zolotyh.planace.db :refer [create-room q-by-ids]]
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.ui :as ui]
   [xtdb.api :as xt]))

(defn room-create-form []
  (biff/form {:hx-swap "innerHTML transition:true"
              :hx-target "#root"
              :hx-post "/app/room/create"
              :hx-indicator "#spinner"
              :class "col-span-12 rounded-sm border border-stroke bg-white px-5 pb-5 pt-7.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:col-span-8 text-slate-900"}
             [:label.block {:for "room"} "Room name"
              [:.h-1]
              [:.flex [:input.w-full#room {:type "text", :name "title", :value ""}]
               [:.w-3] [:button {:type "submit" :class "w-full cursor-pointer rounded-lg border border-primary bg-slate-900 p-4 font-medium text-white transition hover:bg-opacity-90"} "Create"]]]
             [:div#spinner.htmx-indicator "loading"]))



(defn room [room]
  [:li
   [:<>
    [:a {:href (str "/app/room/view/" (str (:xt/id room)))} (:room/title room)]]])

(defn room-list [rooms]
  [:<>
   [:h1.text-xl "Room list"]
   [:ui {:class "max-h-[40vh] overflow-y-auto"} (map room rooms)]])

(defn on-room-create [{:keys [params] :as ctx}]
  (let [room (create-room ctx {:room/title (:title params)})
        redirect-path (str "http://localhost:8080/app/room/view/" (:uuid room))]
    {:status 200
     :headers {"HX-Location" (generate-string {:path redirect-path :swap "innerHTML swap:0.3s transition:true" :targer "#root"})
               "HX-Push-Url" "true"
               "HX-Reswap" "innerHTML swap:1s settle:1s"
               ; "hx-location" redirect-path
               "location" redirect-path}}))



(defn main-page [{:keys [session biff/db] :as ctx}]
  (let [uid (:uid session)
        user (xt/entity db uid)
        rooms (q-by-ids ctx (:user/rooms user))]
    (ui/page ctx (ui/center {:class "h-screen bg-green text-white flex-col"}
                            [:<>
                             (room-list rooms)
                             (room-create-form)
                             [:div "lorem lorem lorem 2"]]))))


(defn room-page [{:keys [path-params biff/db] :as ctx}]
  (let [room (xt/entity db (parse-uuid (:room-id path-params)))]
    (ui/page ctx
             [:<>
              (ui/main-layout
               {:header "header"
                :footer "footer"
                :right-sidebar "right-sidebar"
                :profile "profile"
                :main [:div (:room/title room)]})])))

(def module {:routes ["/app" {:middleware [mid/wrap-signed-in mid/i18n]}
                      ["" {:get main-page}]
                      ["/room"
                       ["/create" {:post on-room-create :conflicting true}]
                       ["/view/:room-id" {:get room-page}]]]})
