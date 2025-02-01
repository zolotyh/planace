(ns com.zolotyh.planace.templates.base
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.settings :as settings]
   [com.zolotyh.planace.templates.utils :as utils]
   [ring.middleware.anti-forgery :as csrf]))

(defn- body [_ & content]
  [:body {:hx-headers (when (bound? #'csrf/*anti-forgery-token*)
                        (cheshire/generate-string {:x-csrf-token csrf/*anti-forgery-token*}))
          :class "flex flex-col h-screen justify-between min-h-screen"} content])

(defn- og-comp [{:keys [title type url image image-alt]}]
  [:<>
   [:meta {:property "og:title", :content title}]
   [:meta {:property "og:type", :content type}]
   [:meta {:property "og:url", :content url}]
   [:meta {:property "og:image", :content image}]
   [:meta {:property "og:image:alt", :content image-alt}]])

(defn- seo [description]
  [:meta {:name "description", :content description}])

(defn- theme-comp [_]
  [:meta {:name "theme-color", :content "#000000"}])

(defn- captcha-comp []
  [:script {:src "https://www.google.com/recaptcha/api.js"
            :async "async" :defer "defer"}])

(defn- scripts-comp []
  [:<>
   [:script {:src (utils/static-path "/js/htmx.min.js")}]
   [:script {:src (utils/static-path "/js/idiomorph-ext.min.js")}]
   [:script {:src (utils/static-path "/js/ws.js")}]
   [:script {:src (utils/static-path "/js/json-enc.js")}]
   [:script {:src (utils/static-path "/js/main.js")}]])

(defn- styles-comp []
  [:<>
   [:link {:rel "stylesheet" :href  (utils/static-path "/css/main.css")}]
   [:link {:rel "stylesheet", :href (utils/static-path "/css/inter.css")}]
   [:link {:rel "stylesheet", :href (utils/static-path "/css/bulma.min.css")}]
   [:link {:rel "stylesheet", :href (utils/static-path "/css/main.css")}]
   [:link {:rel "stylesheet", :href (utils/static-path "/css/fontawesome.min.css")}]])

(defn head [{:keys [::recaptcha og description theme]}]
  [:head {:class "no-js" :lang settings/lang}
   [:meta {:charset "utf-8"}]
   [:meta
    {:name "viewport", :content "width=device-width, initial-scale=1"}]
   [:title "hello"]
   [:link {:rel "icon", :href (utils/static-path "/favicon.ico") , :sizes "any"}]
   [:link {:rel "icon", :href (utils/static-path "/icon.svg") , :type "image/svg+xml"}]
   [:link {:rel "apple-touch-icon", :href (utils/static-path "icon.png")}]
   [:link {:rel "manifest", :href (utils/static-path "/site.webmanifest")}]

   (when recaptcha
     (captcha-comp))

   (when description
     (seo description))

   (when theme
     (theme-comp theme))

   (when og
     (og-comp og))

   (styles-comp)

   (scripts-comp)])

(defn base [ctx & content]
  [:html
   (head ctx)
   (body ctx content)])
