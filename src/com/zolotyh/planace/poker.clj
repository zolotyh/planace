(ns com.zolotyh.planace.poker
  (:require
   [com.zolotyh.planace.settings :as settings]
   [com.zolotyh.planace.ui :as ui]))

(def poker-page
  (ui/page
   {:base/title (str "About " settings/app-name)}
   [:p "This is poker page"]))

(def module
  {:static {"/poker/" poker-page}})
