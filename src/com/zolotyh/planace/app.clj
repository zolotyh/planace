(ns com.zolotyh.planace.app
  (:require
   [cheshire.core :refer [generate-string]]
   [com.biffweb :as biff]
   [com.zolotyh.planace.db :refer [create-room q-by-ids update-room]]
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.ui :as ui]
   [xtdb.api :as xt]))

(defn room-edit-button [room]
  [:div.js-title
   [:span (:room/title room)]
   [:a {:class "inline-block ml-1 mb-1"
        :hx-target "closest .js-title"
        :hx-swap "innerHTML"
        :hx-get  (str "/app/room/form/" (:xt/id room))
        :href (str "/app/room/form/" (:xt/id room))}
    [:img {:src "/img/edit.svg"}]]])

(defn room-name [room]
  [:h3.ml-2.text-2xl.leading-6
   [:span.js-title
    (room-edit-button room)
    [:small.block [:a.text-yellow-500.font-size.text-base {:href "/"} "Planace.ru"]]]])


(defn header [room]
  [:.header#header.flex
   (ui/logo)
   (room-name room)])

(defn on-room-update [{:keys [params, biff/db path-params] :as ctx}]
  (let [room (xt/entity db (parse-uuid (:room-id path-params)))
        updated-room (assoc-in room [:room/title] (:title params))]
    (update-room ctx updated-room)
    (room-edit-button updated-room)))

(defn room-change-name-form [{:keys [path-params biff/db]}]
  (let [room (xt/entity db
                        (parse-uuid (:room-id path-params)))]
    (biff/form {:hx-swap "innerHTML transition:true"
                :hx-target "closest .js-title"
                :hx-post (str "/app/room/update/" (:xt/id room))
                :hx-indicator "#spinner"
                :class "pb-[1.5px] "}

               [:.flex
                [:input
                 {:type "text"
                  :name "title"
                  :autofocus "true"
                  :value (:room/title room)
                  :class "text-white bg-opacity-0 bg-green py-0 px-1 border-white-100"}
                 [:button {:type "submit" :class "w-full cursor-pointer px-5 text-sm rounded ml-3  bg-brand-500 font-xs"} "Update"]
                 [:div#spinner.htmx-indicator "loading"]]])))

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
     :headers {"HX-Location" (generate-string {:path redirect-path :swap "innerHTML swap:0.1s settle:0.3s transition:true" :targer "#root"})
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
               {:header (header room)
                :footer "footer"
                :right-sidebar "right-sidebar"
                :profile "profile"
                :main [:div (:room/title room)]})])))

(def module {:routes ["/app" {:middleware [mid/wrap-signed-in mid/i18n]}
                      ["" {:get main-page}]
                      ["/room"
                       ["/create" {:post on-room-create}]
                       ["/update/:room-id" {:post on-room-update}]
                       ["/form/:room-id" {:get room-change-name-form}]
                       ["/view/:room-id" {:get room-page}]]]})
