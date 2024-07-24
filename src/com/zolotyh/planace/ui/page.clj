(ns com.zolotyh.planace.ui.page
  (:require
   [cheshire.core :as cheshire]
   [com.zolotyh.planace.ui :refer [base]]
   [com.zolotyh.planace.ui.header :refer [header]]
   [ring.middleware.anti-forgery :as csrf]))

(def security-headers
  (when (bound? #'csrf/*anti-forgery-token*)
    {:hx-headers (cheshire/generate-string {:x-csrf-token csrf/*anti-forgery-token*})}))

(defn page [ctx & body]
  (base ctx [:div (merge security-headers {:hx-boost 1 :class "app bg-green text-white font-body bg-[url('/img/noise.svg')]"})
             header
             [:div.mx-auto.px-12.py-8.grid
              body]]))

;; (defn page
;;   [ctx & body]
;;   (base ctx
;;         [:div
;;          (merge
;;           (security-headers)
;;           {:hx-boost 1,
;;            :class
;;            "app bg-green text-white font-body bg-[url('/img/noise.svg')]"})
;;          [:header (header team-name)] [:main.main body]
;;          [:footer {:class ""} footer]]))
