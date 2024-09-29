(ns com.zolotyh.planace.temp
  (:require
   [clojure.string :as str]
   [com.zolotyh.planace.middleware :as mid]
   [com.zolotyh.planace.ui :as ui]))



(def rotation-classes {:left "rotate-6" :right "-rotate-6" :center "rotate-0"})

(def card-colors {:red "bg-brand-500" :blue "bg-blue" :brown "bg-brown"})


(defn card [{:keys [rotate color classes]}]
  (let [static-classes "absolute block top-0 left-0 w-[120px] h-[167px] xl:w-[240px] xl:h-[334px] rounded-md drop-shadow-xl"]
    [:div
     {:class (str/join " "
                       [static-classes
                        (get rotation-classes rotate)
                        (get card-colors color)
                        classes])}

     [:img {:class "absolute top-[50%] left-[50%] translate-y-[-50%] translate-x-[-50%] w-12"
            :src "/img/planace.svg"}]
     [:img {:src "/img/ace.svg" :class "absolute top-2 left-2"}]
     [:img {:src "/img/ace.svg" :class "absolute bottom-2 right-2"}]]))


(def logo
  [:a
   {:class "relative w-[120px] h-[167px] xl:w-[240px] xl:h-[334px] block mx-auto"
    :href "/"}
   (card {:rotate :right :color :brown :classes ""})
   (card {:rotate :left :color :blue})
   (card {:rotate :none :color :red})])


(def slogan
  [:p.block.text-white.text-2xl.text-center.mt-4.font-body.text-shadow.shadow-slate-900 "Level Up Your" [:br] "Planning Poker!"])


(defn main-page [ctx]
  (ui/page
   {}
   [:div
    logo
    slogan]))

(def module {:routes ["/tmp" {:middleware [mid/i18n]}
                      ["" {:get main-page}]]})
