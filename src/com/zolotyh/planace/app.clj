(ns com.zolotyh.planace.app
  (:require
   [cheshire.core :as cheshire]
   [com.biffweb :as biff :refer [q]]
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.settings :as settings]
   [com.zolotyh.planace.ui :as ui :refer [voter-list voters-demo-list]]
   [com.zolotyh.planace.ui.pocker :refer [card]]
   [ring.adapter.jetty9 :as jetty]
   [rum.core :as rum]
   [xtdb.api :as xt]))

(defn create-room [{:keys [session params] :as ctx}]
  (let [id (random-uuid)]
    (biff/submit-tx ctx [{:db/doc-type :room
                          :db/op :create
                          :xt/id id
                          :room/user (:uid  session)
                          :room/name (:name params)
                          :room/created-at :db/now}])
    {:status 200
     :headers {"HX-Location" (str "http://localhost:8080/app/room/" id)}}))

(defn room-form [{:keys [value]}]
  (biff/form
   {:hx-post "/app/create-room"}
   [:label.block {:for "name"} "Room name"
    [:span.font-mono (pr-str value)]]
   [:.h-1]
   [:.flex
    [:input.w-full#bar {:type "text" :name "name" :value value}]
    [:.w-3]
    [:button.btn {:type "submit"} "Update"]]
   [:.h-1]
   [:.text-sm.text-gray-600
    "This demonstrates updating a value with HTMX."]))

(defn bar-form [{:keys [value]}]
  (biff/form
   {:hx-post "/app/set-bar"
    :hx-swap "outerHTML"}
   [:label.block {:for "bar"} "Bar: "
    [:span.font-mono (pr-str value)]]
   [:.h-1]
   [:.flex
    [:input.w-full#bar {:type "text" :name "bar" :value value}]
    [:.w-3]
    [:button.btn {:type "submit"} "Update"]]
   [:.h-1]
   [:.text-sm.text-gray-600
    "This demonstrates updating a value with HTMX."]))

(defn message [{:msg/keys [text sent-at]}]
  [:.mt-3 {:_ "init send newMessage to #message-header"}
   [:.text-gray-600 (biff/format-date sent-at "dd MMM yyyy HH:mm:ss")]
   [:div text]])

(defn notify-clients [{:keys [com.zolotyh.planace/chat-clients]} tx]
  (doseq [[op & args] (::xt/tx-ops tx)
          :when (= op ::xt/put)
          :let [[doc] args]
          :when (contains? doc :msg/text)
          :let [html (rum/render-static-markup
                      [:div#messages {:hx-swap-oob "afterbegin"}
                       (message doc)])]
          ws @chat-clients]
    (jetty/send! ws html)))

(defn send-message [{:keys [session] :as ctx} {:keys [text]}]
  (let [{:keys [text]} (cheshire/parse-string text true)]
    (biff/submit-tx ctx
                    [{:db/doc-type :msg
                      :msg/user (:uid session)
                      :msg/text text
                      :msg/sent-at :db/now}])))

(defn chat [{:keys [biff/db]}]
  (let [messages (q db
                    '{:find (pull msg [*])
                      :in [t0]
                      :where [[msg :msg/sent-at t]
                              [(<= t0 t)]]}
                    (biff/add-seconds (java.util.Date.) (* -60 10)))]
    [:div {:hx-ext "ws" :ws-connect "/app/ws"}
     [:form.mb-0 {:ws-send true
                  :_ "on submit set value of #message to ''"}
      [:label.block {:for "message"} "Write a message"]
      [:.h-1]
      [:textarea.w-full#message {:name "text"}]
      [:.h-1]
      [:.text-sm.text-gray-600
       "Sign in with an incognito window to have a conversation with yourself."]
      [:.h-2]
      [:div [:button.btn {:type "submit"} "Send message"]]]
     [:.h-6]
     [:div#message-header
      {:_ "on newMessage put 'Messages sent in the past 10 minutes:' into me"}
      (if (empty? messages)
        "No messages yet."
        "Messages sent in the past 10 minutes:")]
     [:div#messages
      (map message (sort-by :msg/sent-at #(compare %2 %1) messages))]]))

(defn app [{:keys [session biff/db] :as ctx}]
  (let [{:user/keys [email foo bar]} (xt/entity db (:uid session))]
    (ui/page
     {}
     [:div "Signed in as " email ". "
      (card 1)
      (biff/form
       {:action "/auth/signout"
        :class "inline"}
       [:button.text-blue-500.hover:text-blue-800 {:type "submit"}
        "Sign out"])
      "."]
     (room-form {:value ""})
     (chat ctx))))

(defn add-new-connection [room-id user-id collection conn]
  (update-in collection [(keyword room-id) (keyword user-id)] conn))

(defn ws-room [{:keys [com.zolotyh.planace/chat-clients path-params] :as ctx}]
  {:status 101
   :headers {"upgrade" "websocket"
             "connection" "upgrade"}
   :ws {:on-connect (fn [ws]
                      (swap! chat-clients conj ws))
        :on-text (fn [ws text-message]
                   (send-message ctx {:ws ws :text text-message}))
        :on-close (fn [ws status-code reason]
                    (swap! chat-clients disj ws))}})

(defn ws-handler [{:keys [com.zolotyh.planace/chat-clients] :as ctx}]
  {:status 101
   :headers {"upgrade" "websocket"
             "connection" "upgrade"}
   :ws {:on-connect (fn [ws]
                      (swap! chat-clients conj ws))
        :on-text (fn [ws text-message]
                   (send-message ctx {:ws ws :text text-message}))
        :on-close (fn [ws status-code reason]
                    (swap! chat-clients disj ws))}})

(def about-page
  (ui/page
   {:base/title (str "About " settings/app-name)}
   [:p "This app was made with "
    [:a.link {:href "https://biffweb.com"} "Biff"] "."]))

(defn echo [{:keys [params]}]
  {:status 200
   :headers {"content-type" "application/json"}
   :body params})

(defn- render-room [{:keys [path-params biff/db]}]
  (let [room (xt/entity db (parse-uuid
                            (:token path-params)))]
    (ui/page
     {:base/title (str (:token path-params))}
     (voter-list voters-demo-list)
     [:p "room" (:room/name room)])))

(def module
  {:static {"/about/" about-page}
   :routes ["/app" {:middleware [mid/wrap-signed-in]}
            ["" {:get app}]
            ["/room/:token" {:get render-room}]
            ["/create-room" {:post create-room}]
            ["/ws/:room-id" {:get ws-room}]
            ["/ws" {:get ws-handler}]]
   :api-routes [["/api/echo" {:post echo}]]
   :on-tx notify-clients})
