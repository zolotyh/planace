(ns com.zolotyh.planace.ui.room.footer)

(defn vote-button
  [{value :value, title :title, active :active}]
  (if active
    [:div
     {:class
      "text-white bg-red text-2xl font-georgia flex justify-center items-center aspect-[98/136] -mt-[10%] rounded-xl"}
     [:div title]]
    [:div
     {:class
      "bg-white cursor-pointer text-black text-2xl font-georgia flex justify-center items-center aspect-[98/136] -mt-[0%] rounded-xl"}
     [:div title]]))

(def footer
  [:footer {:class ""}
   [:div {:class "mx-auto px-12 py-8 "}
    [:div {:class "grid grid-cols-18 gap-2"}
     (vote-button {:vote 1, :title "pass"})
     (vote-button {:vote 1, :title 1, :active true})
     (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
     (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
     (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
     (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
     (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
     (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})]]])
