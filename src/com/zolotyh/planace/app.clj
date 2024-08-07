(ns com.zolotyh.planace.app
  (:require
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.ui :as ui]))

(defn app [{:keys [i18n] :as ctx}]
  (ui/page ctx
           [:<>
            (ui/main-layout
             {:header "header"
              :footer "footer"
              :right-sidebar "right-sidebar"
              :main "main"})]))
            ; [:div (i18n [:page/title])]
            ; (ui/vote-results)
            ; (ui/vote-panel)]))

(def module {:routes ["/app" {:middleware [mid/wrap-signed-in mid/i18n]}
                      ["" {:get app}]]})
