(ns com.zolotyh.planace.app
  (:require
   [com.zolotyh.planace.middleware :as mid]))

(defn- app [{:keys [i18n]}]
  [:div (i18n [:page/title])])

(def module {:routes ["/app" {:middleware [mid/wrap-signed-in mid/i18n]}
                      ["" {:get app}]]})
