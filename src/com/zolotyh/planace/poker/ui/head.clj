(ns com.zolotyh.planace.poker.ui.head
  (:require
   [clojure.java.io :as io]
   [ring.util.response :as ring-response]))

(defn static-path [path]
  (if-some [last-modified (some-> (io/resource (str "public" path))
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str path "?t=" last-modified)
    path))

(def styles
  [:<>
   [:link {:rel "stylesheet", :href (static-path "/css/inter.css")}]
   [:link {:rel "stylesheet", :href (static-path "/css/bulma.min.css")}]
   [:link {:rel "stylesheet", :href (static-path "/css/main.css")}]
   [:link {:rel "stylesheet", :href (static-path "/css/font-awesome.min.css")}]])

(def scripts
  [:<>
   [:script {:src (static-path "/js/htmx.min.js")}]
   [:script {:src (static-path "/js/alpine.min.js")}]
   [:script {:src (static-path "/js/idiomorph-ext.min.js")}]
   [:script {:src (static-path "/js/json-enc.js")}]
   [:script {:src (static-path "/js/main.js")}]
   [:script {:src (static-path "/js/ws.js")}]])

(defn head [{:keys [title base-url meta-description]}]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport", :content "width=device-width, initial-scale=1"}]
   [:title title]
   styles
   scripts
   [:meta {:name "description", :content meta-description}]
   [:meta {:property "og:title", :content title}]
   [:meta {:property "og:type", :content "website"}]
   [:meta {:property "og:url", :content base-url}]
   [:meta {:property "og:image", :content "icon.png"}]
   [:meta {:property "og:image:alt", :content ""}]
   [:link {:rel "icon", :href "/favicon.ico", :sizes "any"}]
   [:link {:rel "icon", :href "/icon.svg", :type "image/svg+xml"}]
   [:link {:rel "apple-touch-icon", :href "icon.png"}]
   [:link {:rel "manifest", :href "/site.webmanifest"}]
   [:meta {:name "theme-color", :content "#fafafa"}]])
