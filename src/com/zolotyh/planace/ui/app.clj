(ns com.zolotyh.planace.ui.app
  (:require [clojure.string :as str]))

(defn get-abbr
  [{first-name :first-name, last-name :last-name}]
  (str/upper-case (str (first first-name) (first last-name))))

(defn voted
  [{:keys [first-name last-name vote]}]
  [:div.px-3.py-2 first-name last-name vote])

(defn not-voted
  [{:keys [first-name last-name vote]}]
  [:div.px-3.px-2 first-name last-name vote])

(defn avatar
  [user]
  (if (:avatar-url user)
    [:div.rounded-full.shadow-xl.overflow-hidden.w-12.h-12.color-red
     [:img {:src (:avatar-url user), :alt (get-abbr user)}]]
    [:div
     {:class
      "text-xl font-georgia rounded-full text-black h-12 w-12 flex justify-center items-center"}
     (get-abbr user)]))

;; (defn voter [{:keys [voted?], :as ctx}] (if voted? (voted ctx) (not-voted
;; ctx)))
(defn card
  [{:keys [first-name last-name vote], :as ctx}]
  [:div {:title (str first-name " " last-name)}
   [:div
    {:class
     "rounded-xl bg-white aspect-[98/136] shadow-xl flex justify-center relative items-center"}
    (avatar ctx)
    [:div
     {:class
      "text-black font-thin text-xl font-bold font-georgia absolute top-1 left-2"}
     (:value vote)]
    [:div
     {:class
      "text-black font-thin text-xl font-bold font-georgia absolute bottom-1 right-2 transform rotate-180"}
     (:value vote)]]
   [:div.mt-1.text-center.p-1.text-ellipsis.overflow-hidden first-name [:br]
    last-name]])

(defn voter-list
  [voters-list]
  [:div.px-2.py-1.d-flex.flex-row (map card voters-list)])


