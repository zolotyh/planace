(ns com.zolotyh.planace.pages.main
  (:require
   [com.zolotyh.planace.templates.main :as tpl]))

(defn signed-in? [_]
  true)

(defn main [ctx]
  (tpl/main ctx {:main "main"}))

