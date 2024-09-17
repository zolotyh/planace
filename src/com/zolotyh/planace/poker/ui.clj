(ns com.zolotyh.planace.poker.ui)


(defn vote-result-item [_ctx value]
  [:li (str "vote-result-item: " value)])

(defn vote-result [{:keys [vote-result-items] :as ctx}]
  [:ul
   (map (partial vote-result-item ctx) vote-result-items)])


(defn vote-panel-item [_ctx value]
  [:div (str
         "vote-panel-item" value)])

(defn vote-panel [{:keys [vote-panel-items] :as ctx}]
  [:ul
   (map (partial vote-panel-item ctx) vote-panel-items)])

(defn room-list-item [{:keys [] :as _ctx} value]
  [:li
   (str
    "room-list-item "
    value)])

(defn room-search [{:keys [] :as _ctx}]
  [:div "room-search"])

(defn room-list [{:keys [room-list] :as ctx}]
  [:div
   [:div.room-search
    (room-search ctx)]
   [:ul.list
    (map (partial room-list-item ctx) room-list)]])

(defn room [{:keys [] :as ctx}]
  [:div.room
   (vote-result ctx)
   (room-list ctx)
   (vote-panel ctx)])


(room {:room-list (range 3)
       :vote-result-items (range 3)
       :vote-panel-items (range 3)})


