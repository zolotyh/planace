(ns com.zolotyh.planace.templates.main
  (:require
   [com.zolotyh.planace.templates.base :as tpl]
   [com.zolotyh.planace.settings :as settings]))

(defn- default-header []
  [:<>
   [:h1 {:class "text-xl mb-0 font-normal"} settings/app-name]
   [:p {:class "text-xs font-extralight opacity-80"} settings/slogan]])

(defn- default-footer []
  [:<>
   [:p {:class "text-xs font-extralight opacity-80 text-center"}
    (str
     "All right reserved ©"
     (.format (java.text.SimpleDateFormat. "yyyy") (new java.util.Date)))]])

(defn main [ctx {:keys [main footer header]}]
  (tpl/base
   ctx
   [:<>
    [:header {:id "header" :class ""} (if header header (default-header))]
    [:main   {:id "main"   :class ""} main]
    [:footer {:id "footer" :class "flex items-center justify-center"} (if footer footer (default-footer))]]))


