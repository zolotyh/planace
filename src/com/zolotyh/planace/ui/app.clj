(ns com.zolotyh.planace.ui.app)

(def voters-demo-list
  (take 10 (iterate identity {:voted true :first-name "John" :last-name "Doe" :vote 1})))

(defn voted [{:keys [first-name last-name vote]}]
  [:div.px-3.py-2 first-name last-name vote])

(defn not-voted [{:keys [first-name last-name vote]}]
  [:div.px-3.px-2 first-name last-name vote])

(defn voter [{:keys [voted?] :as ctx}]
  (if voted?
    (voted ctx) (not-voted ctx)))

(defn voter-list [voters-list]
  [:div.px-2.py-1.d-flex.flex-row (map voter voters-list)])
