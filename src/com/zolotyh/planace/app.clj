(ns com.zolotyh.planace.app
  (:require [cheshire.core :as cheshire]
            [com.biffweb :as biff :refer [q]]
            [com.zolotyh.planace.middleware :as mid]
            [com.zolotyh.planace.pocker :refer [poker]]
            [com.zolotyh.planace.settings :as settings]
            [com.zolotyh.planace.ui :as ui]
            [com.zolotyh.planace.ui.app :refer [voter-list]]
            [com.zolotyh.planace.ui.ui :refer [layout]]
            [ring.adapter.jetty9 :as jetty]
            [rum.core :as rum]
            [xtdb.api :as xt]))

(defn create-room
  [{:keys [session params], :as ctx}]
  (let [id (random-uuid)]
    (biff/submit-tx ctx
                    [{:db/doc-type :room,
                      :db/op :create,
                      :xt/id id,
                      :room/user (:uid session),
                      :room/name (:name params),
                      :room/created-at :db/now}])
    {:status 200,
     :headers {"HX-Location" (str "http://localhost:8080/app/room/" id)}}))

(defn room-form
  [{:keys [value]}]
  (biff/form {:hx-post "/app/create-room"}
             [:label.block {:for "name"} "Room name"]
             [:.flex
              [:input.w-full#bar {:type "text", :name "name", :value value}]
              [:.w-3] [:button.btn.btn-primary {:type "submit"} "Update"]]))

(defn message
  [{:msg/keys [text sent-at]}]
  [:.mt-3 {:_ "init send newMessage to #message-header"}
   [:.text-gray-600 (biff/format-date sent-at "dd MMM yyyy HH:mm:ss")]
   [:div text]])

(defn notify-clients
  [{:keys [com.zolotyh.planace/chat-clients]} tx]
  (doseq [[op & args] (::xt/tx-ops tx)
          :when (= op ::xt/put)
          :let [[doc] args]
          :when (contains? doc :msg/text)
          :let [html (rum/render-static-markup [:div#messages
                                                {:hx-swap-oob "afterbegin"}
                                                (message doc)])]
          ws @chat-clients]
    (jetty/send! ws html)))

(defn send-message
  [{:keys [session], :as ctx} {:keys [text]}]
  (let [{:keys [text]} (cheshire/parse-string text true)]
    (biff/submit-tx ctx
                    [{:db/doc-type :msg,
                      :msg/user (:uid session),
                      :msg/text text,
                      :msg/sent-at :db/now}])))

(defn chat
  [{:keys [biff/db]}]
  (let [messages (q db
                    '{:find (pull msg [*]),
                      :in [t0],
                      :where [[msg :msg/sent-at t] [(<= t0 t)]]}
                    (biff/add-seconds (java.util.Date.) (* -60 10)))]
    [:div {:hx-ext "ws", :ws-connect "/app/ws"}]))

(defn app
  [{:keys [session biff/db], :as ctx}]
  (let [{:user/keys [email foo bar], as ctx} (xt/entity db (:uid session))]
    (ui/page {}
             [:div "Signed in as " email ". " (:uid session)
              (biff/form {:action "/auth/signout", :class "inline"}
                         [:button.text-blue-500.hover:text-blue-800
                          {:type "submit"} "Sign out"]) "."]
             (room-form {:value ""}))))

(defn ws-handler
  [{:keys [com.zolotyh.planace/chat-clients], :as ctx}]
  {:status 101,
   :headers {"upgrade" "websocket", "connection" "upgrade"},
   :ws {:on-connect (fn [ws] (swap! chat-clients conj ws)),
        :on-text (fn [ws text-message]
                   (send-message ctx {:ws ws, :text text-message})),
        :on-close (fn [ws status-code reason] (swap! chat-clients disj ws))}})

(def about-page
  (ui/page {:base/title (str "About " settings/app-name)}
           (layout)
           [:p "This app was made with "
            [:a.link {:href "https://biffweb.com"} "Biff"] "."]))

(defn echo
  [{:keys [params]}]
  {:status 200, :headers {"content-type" "application/json"}, :body params})

(defn render-room
  [{:keys [path-params biff/db]}]
  (let [room (xt/entity db (parse-uuid (:room-id path-params)))
        vote (xt/entity db (:room/active-vote room))]
    (ui/page {:base/title (str (:token path-params))}
             (voter-list (vals (:vote/results vote)))
             [:div "vote: " (:vote/title vote) "active vote id: "
              (:room/active-vote room)]
             [:p "room: " (:room/name room)])))

(def module
  {:static {"/about/" about-page},
   :routes ["/app" {:middleware [mid/wrap-signed-in]} ["" {:get app}]
            ["/poker" {:get poker}] ["/room/:room-id" {:get render-room}]
            ["/create-room" {:post create-room}] ["/ws" {:get ws-handler}]],
   :api-routes [["/api/echo" {:post echo}]],
   :on-tx notify-clients})
