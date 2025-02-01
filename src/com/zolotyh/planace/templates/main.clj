(ns com.zolotyh.planace.templates.main
  (:require
   [com.zolotyh.planace.templates.base :as tpl]
   [com.zolotyh.planace.settings :as settings]))

(defn- default-header []
  [:div.hero-body
   [:h1 {:class "text-xl mb-0 font-normal"} settings/app-name]
   [:p {:class "text-xs font-extralight opacity-80"} settings/slogan]])

(defn- default-footer []
  [:div {:class "hero-body"}
   [:p {:class "text-xs font-extralight opacity-80 text-center"}
    (str
     "All right reserved ©"
     (.format (java.text.SimpleDateFormat. "yyyy") (new java.util.Date)))]])

(defn main [ctx {:keys [main footer header]}]
  (tpl/base
   ctx
   [:<>
    [:header {:id "header" :class "hero"} (if header header (default-header))]
    [:main   {:id "main"   :class "flex-grow hero"} [:div.hero-body main]]
    [:footer {:id "footer" :class "hero"} (if footer footer (default-footer))]]))


