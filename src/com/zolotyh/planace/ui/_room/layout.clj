(ns com.zolotyh.planace.ui.room.layout
  (:require [com.zolotyh.planace.ui.room.vote-list :as vote-list]
            [com.zolotyh.planace.ui.room.vote-result :refer [vote-result]]
            [com.zolotyh.planace.ui.ui :refer [task-header]]))

(defn layout
  []
  [:div.mx-auto.px-12.py-8.grid
   [:div.grid.grid-flow-row.grid-cols-12.gap-4
    [:div.col-span-8.grid-cols-7.gap-x-8.grid
     [:div.col-span-5
      (task-header
       {:title
        "Lorem ipsum dolor sit amet, qui minim labore adipisicing minim sint cillum sint consectetur cupidatat.",
        :code "WTE-23455"})]
     [:div.col-span-2
      [:h3.text-4xl.text-right
       [:div.inline-block.align-middle "Result:" [:p.text-lg.text-left "reset"]]
       [:div.inline-block.relative.ml-2.w-24.h-24.align-middle
        [:img
         {:class
          "align-middle pr-2 w-24 h-24 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2",
          :src "/img/chips.svg"}]
        [:span
         {:class
          "font-georgia absolute w-full -mt-2 -ml-1 text-center top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-3xl"}
         2]]]]]
    [:div.grid.grid-flow-row.grid-cols-7.gap-x-8.gap-y-6.col-span-8.content-start
     (vote-result) [:div {:class "col-span-4 px-12"} (vote-list/vote-list)]]]])
