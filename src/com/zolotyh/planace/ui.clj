(ns com.zolotyh.planace.ui
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [com.biffweb :as biff]
            [com.zolotyh.planace.settings :as settings]
            [ring.middleware.anti-forgery :as csrf]
            [ring.util.response :as ring-response]
            [rum.core :as rum]))

; /* Ellipse 1 */
;
; position: absolute;
; width: 51px;
; height: 51px;
; left: 27px;
; top: 31px;
;
; background: #B6433C;
; box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);


(defn logo []
  [:div {:class "
         bg-[url('/img/noise.svg')]
         bg-brand-500
         w-[50px]
         h-[50px]
         shadow-logoOuter
         rounded-full
         relative
         shadow-xl
"}
   [:div {:class
          "bg-brand-500 
         bg-[url('/img/noise.svg')]
          w-[38px]
          h-[38px]
          shadow-logoInner
          rounded-full 
          absolute 
          left-[50%] 
          top-[50%] 
          translate-x-[-50%] 
          translate-y-[-50%] "}]])



(defn center [a1 a2]
  (let [{class :class :or {class ""}} a1
        elem (if (vector? a1) a1 a2)]
    [:div {:class (str "flex content-center items-center justify-center " class)} elem]))

(defn vote-info [vote]
  [:div.text-red-700 "vote info"])

(defn vote-panel-item [_]
  [:div.text-xl.text-color-red "test"])

(defn vote-result-item [value]
  [:div.text-white.bg-slate-600 value])

(defn vote-panel []
  [:div.text-xl.px-5.py-2.bg-slate-900.text-white.mb-4.mt-10 (map vote-panel-item (range 10))])


(defn vote-results []
  [:<>
   (vote-info {})
   [:div.text-xl.px-5.py-2.bg-slate-900.text-white "Vote results"]
   [:div.mb-8
    (map vote-result-item (range 10))]])

(defn main-layout [{:keys [header right-sidebar main footer profile]}]
  [:div {:class "wrapper h-screen w-screen bg-green font-body bg-[url('/img/noise.svg')]"}
   [:div {:class "header px-10 py-10  text-white"} header]
   [:div {:class "profile pt-10 pr-10  text-white"} profile]
   [:div {:class "main pl-10  text-white"} main]
   [:div {:class "right-sidebar  text-white"} right-sidebar]
   [:div {:class "footer px-10 py-10  bg-green-900 text-white"} footer]])


(defn css-path [path]
  (if-some [last-modified (some-> (io/resource (str "public/" path))
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str path "?t=" last-modified)
    path))

(defn js-path [path]
  (if-some [last-modified (some-> (io/resource (str "public" path))
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str path "?t=" last-modified) path))

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
                            (concat [[:link {:rel "stylesheet" :href (css-path "/css/main.css")}]
                                     [:link {:rel "stylesheet" :href (css-path "/css/animate.css")}]
                                     [:script {:src (js-path "/js/htmx.js")}]
                                     [:script {:src (js-path "/js/ws.js")}]
                                     [:script {:src (js-path "/js/main.js")}]
                                     [:script {:src (js-path "/js/_hyperscript.min.js")}]
                                     (when recaptcha
                                       [:script {:src "https://www.google.com/recaptcha/api.js"
                                                 :async "async" :defer "defer"}])]
                                    head))))
   body))

(defn page [ctx & body]
  (base
   ctx
   [:div#root.sample-transition.h-screen.flex.items-center.justify-center
    (when (bound? #'csrf/*anti-forgery-token*)
      {:hx-headers (cheshire/generate-string
                    {:x-csrf-token csrf/*anti-forgery-token*})})
    body]))

(defn on-error [{:keys [status ex] :as ctx}]
  {:status status
   :headers {"content-type" "text/html"}
   :body (rum/render-static-markup
          (page
           ctx
           [:h1.text-lg.font-bold
            (if (= status 404)
              "Page not found."
              "Something went wrong.")]))})
