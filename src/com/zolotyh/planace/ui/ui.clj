(ns com.zolotyh.planace.ui.ui
  (:require [cheshire.core :as cheshire]
            [clojure.core :refer [bound?]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [com.biffweb :as biff]
            [com.zolotyh.planace.ui.mocks :as mocks]
            [ring.middleware.anti-forgery :as csrf]))


(def domain "scrumcasino.io")
(def home-link (str "https://" domain))
(def team-name "WTE team")

(def example-vote {:value 10})

(defn task-item
  [task]
  [:div.mb-4.flex.flex-row.justify-center.items-center
   [:div.pr-2.col-span-2 [:img.pr-2.col-span-2 {:src "/img/chips.svg"}]]
   [:div.col-span-6.overflow-hidden.text-ellipsis.whitespace-nowrap
    (:title task)] [:div "edit"]])

(defn task-list [] [:ul (for [task mocks/tasks] (task-item task))])

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

(def example-user
  {:avatar-url
     "https://sun9-73.userapi.com/impg/gM-PcbLDL3SATO8cWYwk6VJRAEH-Fwt98o-zRg/PpC3EJYSl5I.jpg?size=791x933&quality=95&sign=c6d4afd3b062eb11005cca0fb03abfd9&type=album",
   :first-name "alekseifdfsdfsdfsdfdsfsdfdsfsdf",
   :last-name "Zolotykhsdfsdfsdsdfsdfsdfsdfsdf"})

(defn get-abbr
  [{first-name :first-name, last-name :last-name}]
  (str/upper-case (str (first first-name) (first last-name))))

(defn task-header
  [{title :title, code :code}]
  [:h2.mb-12 [:span.text-md.text-yellow code] [:br]
   [:div.text-3xl.text-ellipsis.overflow-hidden.h-9.w-full.whitespace-nowrap
    {:title title} title]])

(defn avatar
  [user]
  (if (:avatar-url user)
    [:div.rounded-full.shadow-xl.overflow-hidden.w-12.h-12.color-red
     [:img {:src (:avatar-url user), :alt (get-abbr user)}]]
    [:div
     {:class
        "text-xl font-georgia rounded-full text-black h-12 w-12 flex justify-center items-center"}
     (get-abbr user)]))

(defn card
  [user vote]
  [:div {:title (str (:first-name user) " " (:last-name user))}
   [:div
    {:class
       "rounded-xl bg-white aspect-[98/136] shadow-xl flex justify-center relative items-center"}
    (avatar user)
    [:div
     {:class
        "text-black font-thin text-xl font-bold font-georgia absolute top-1 left-2"}
     (:value vote)]
    [:div
     {:class
        "text-black font-thin text-xl font-bold font-georgia absolute bottom-1 right-2 transform rotate-180"}
     (:value vote)]]
   [:div.mt-1.text-center.p-1.text-ellipsis.overflow-hidden (:first-name user)
    [:br] (:last-name user)]])

(def main
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
     (card example-user (assoc example-vote :value "pass"))
     (card example-user example-vote) (card example-user example-vote)
     (card example-user example-vote) (card example-user example-vote)
     (card example-user (assoc example-vote :active true))
     (card example-user example-vote) (card example-user example-vote)
     (card example-user example-vote) (card example-user example-vote)
     (card example-user example-vote) (card example-user example-vote)
     (card (dissoc example-user :avatar-url) example-vote)]
    [:div {:class "col-span-4 px-12"} (task-list)]]])

(def footer
  [:div {:class "mx-auto px-12 py-8 "}
   [:div {:class "grid grid-cols-18 gap-2"}
    (vote-button {:vote 1, :title "pass"})
    (vote-button {:vote 1, :title 1, :active true})
    (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
    (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
    (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
    (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
    (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})
    (vote-button {:vote 1, :title 1}) (vote-button {:vote 1, :title 1})]])

(defn header
  [name]
  [:div.mx-auto.px-12.py-8.grid.grid-cols-12.auto-cols-max.gap-2
   [:div.col-span-5.flex.flex-row
    [:a.block.mt-1 {:href "/"}
     [:img.block.h-18.w-18 {:src "/img/chips.svg", :alt "scrumcasino logo"}]]
    [:div.ml-2 [:p.text-3xl name]
     [:p.text-lg [:a.text-yellow {:href home-link} domain]]]]
   [:div {:class "text-lg col-start-10 col-end-13 text-right"}
    "Константин Константинопольский"]])

(defn layout
  []
  [:div {:class "app bg-green text-white font-body bg-[url('/img/noise.svg')]"}
   [:header {:class "header"} (header team-name)] [:main {:class "main"} main]
   [:footer {:class "footer bg-darkgreen bg-[url('/img/noise.svg')] z-10"}
    footer]])

(defn css-path
  []
  (if-some [f (io/file (io/resource "public/css/main.css"))]
    (str "/css/main.css?t=" (.lastModified f))
    "/css/main.css"))

