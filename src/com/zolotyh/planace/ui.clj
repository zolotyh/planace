(ns com.zolotyh.planace.ui
  (:require
   [cheshire.core :as cheshire]
   [clojure.java.io :as io]
   [com.biffweb :as biff]
   [com.zolotyh.planace.poker.ids :as ids]
   [com.zolotyh.planace.poker.ui.footer :as footer]
   [com.zolotyh.planace.poker.ui.header :as header]
   [com.zolotyh.planace.settings :as settings]
   [ring.middleware.anti-forgery :as csrf]
   [ring.util.response :as ring-response]
   [rum.core :as rum]))

(defn static-path [path]
  (if-some [last-modified (some-> (io/resource (str "public" path))
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str path "?t=" last-modified)
    path))

(defn base [{:keys [::recaptcha] :as ctx} & body]
  (apply
   biff/base-html
   (-> ctx
       (merge #:base{:title settings/app-name
                     :lang "en-US"
                     :icon "/img/glider.png"
                     :description (str settings/app-name " Description")
                     :image "https://clojure.org/images/clojure-logo-120b.png"})
       (update :base/head (fn [head]
                            (concat [[:link {:rel "stylesheet" :href (static-path "/css/main.css")}]
                                     [:link {:rel "stylesheet", :href (static-path "/css/inter.css")}]
                                     [:link {:rel "stylesheet", :href (static-path "/css/bulma.min.css")}]
                                     [:link {:rel "stylesheet", :href (static-path "/css/main.css")}]
                                     [:link {:rel "stylesheet", :href (static-path "/css/fontawesome.min.css")}]
                                     [:script {:src (static-path "/js/htmx.min.js")}]
                                     [:script {:src (static-path "/js/idiomorph-ext.min.js")}]
                                     [:script {:src (static-path "/js/ws.js")}]
                                     [:script {:src (static-path "/js/json-enc.js")}]
                                     [:script {:src (static-path "/js/main.js")}]
                                     (when recaptcha
                                       [:script {:src "https://www.google.com/recaptcha/api.js"
                                                 :async "async" :defer "defer"}])]
                                    head))))
   body))

(defn page [ctx & body]
  (base
   ctx
   [:div
    (when (bound? #'csrf/*anti-forgery-token*)
      {:hx-headers (cheshire/generate-string
                    {:x-csrf-token csrf/*anti-forgery-token*})})
    [:div {:class "flex flex-col h-screen justify-between min-h-screen"}
     (header/header-template {})
     [:div {:class "flex-grow.px-12.pb-40"
            :id ids/root :hx-ext "morph"} body]
     (footer/footer-template {})]]))

; (defn main-template [main]
;   [:main.flex-grow.px-12.pb-40
;    main])
;
; (defn container [& content]
;   [:html
;    [:head
;     (head/head {:title "hello"})]
;    [:body.flex.flex-col.h-screen.justify-between.min-h-screen
;     content]])

(defn on-error [{:keys [status _ex] :as ctx}]
  {:status status
   :headers {"content-type" "text/html"}
   :body (rum/render-static-markup
          (page
           ctx
           [:h1.text-lg.font-bold
            (if (= status 404)
              "Page not found."
              "Something went wrong.")]))})
