(ns com.zolotyh.planace.ui.header)

(def header
  [:header {:class header}
   [:div.mx-auto.px-12.py-8.grid.grid-cols-12.auto-cols-max.gap-2
    [:div.col-span-5.flex.flex-row
     [:a.block.mt-1 {:href "/"}
      [:img.block.h-18.w-18 {:src "/img/chips.svg", :alt "scrumcasino logo"}]]
     [:div.ml-2 [:p.text-3xl "Planace"]
      [:p.text-lg
       [:a.text-yellow {:href "http://localhost:3000"} "planeace.ru"]]]]
    [:div.text-lg.col-start-10.col-end-13.text-right
     "Константин Константинопольский"]]])
