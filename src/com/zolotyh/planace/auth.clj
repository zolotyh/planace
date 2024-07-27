(ns com.zolotyh.planace.auth
  (:require
   [com.zolotyh.planace.middleware :as mid]))

(defn signin-page [{:keys [] :as ctx}]
  [:div "test"])

(def module
  {:routes [["/custom-auth" {:middleware [mid/wrap-redirect-signed-in]}
             [""                  {:get signin-page}]]]})
