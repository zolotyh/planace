(ns com.zolotyh.planace.pocker
  (:require
   [com.zolotyh.planace.ui :as ui]))

(defn poker [{:keys [session biff/db] :as ctx}]
  (ui/page
   {}
   [:p "Hello world"]))
