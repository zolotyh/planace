(ns com.zolotyh.planace.i18n
  (:require [taoensso.tempura :as tempura :refer [tr]]))

(def tconfig
  {:dict
   {:en-US
    {:missing ":en-US missing text"
     :page {:title "Here is a title"
            :content "Time to start building your site."}}
    :fr-FR
    {:page {:title "Voici un titre"
            :content "Il est temps de commencer votre site."}}}})


(def i18n (partial tr tconfig))
