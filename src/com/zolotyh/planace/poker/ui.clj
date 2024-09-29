(ns com.zolotyh.planace.poker.ui
  (:require
   [com.zolotyh.planace.sequences :refer [sequences]]))


(defn result-item [ctx, value]
  [:div "result item"])

(defn result [ctx]
  (map (partial result-item ctx) (:results ctx)))


(defn search [ctx]
  [:div "search"])

(defn task-item [ctx value]
  [:div "task item"])

(defn task-switcher [ctx]
  [:div
   (search ctx)
   (map (partial task-item ctx) (:tasks ctx))])


(defn voting-panel-item [ctx [value]]
  [:div (str "v-item" value)])

(defn voting-panel [{:keys [sequence] :as ctx}]
  [:div.voting-panel
   (map (partial voting-panel-item ctx) sequence)])


(defn poker-page [ctx]
  [:<>
   (result ctx)
   (task-switcher ctx)
   (voting-panel ctx)])


(poker-page {:sequence (:fib sequences)
             :tasks (range 1)
             :results (range 1)})
