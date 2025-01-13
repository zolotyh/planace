(ns com.zolotyh.planace.poker.delete
  (:require
   [com.rpl.specter :as s]))

(defn data []
  (->> (range 10)
       (map #(hash-map :val % :user %))))

(data)

(defn update-result [{:keys [user val]} data]
  (reduce (fn [res v]
            (print res)
            (conj res
                  (if (= (:user v) user)
                    (merge v {:val val})
                    v)))
          [] data))

(update-result {:user 5 :val 10} (data))


