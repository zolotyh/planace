(ns com.zolotyh.planace.app
  (:require
   [com.zolotyh.planace.middleware :as mid]))

(defn- app [arg1]
  [:div "test"])


(def module {:routes ["/app" {:middleware [mid/wrap-signed-in]}
                      ["" {:get app}]]})
