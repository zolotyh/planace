(ns com.zolotyh.planace.poker.voting
  (:require
   [clojure.core :as c]))

(defn vote-reducer [new-result memo current-result]
  (let [options (:options memo)]
    (if (= (:user new-result) (:user current-result))
      {:has-user? true :options
       (conj options
             (merge current-result new-result))}
      {:has-user? (:has-user? memo)
       :options (conj options current-result)})))

(defn vote [new-result vote-data]
  (let [default {:options []
                 :has-user? false}
        updated-vote-data (reduce
                           (partial vote-reducer new-result) default vote-data)
        updated-options (:options updated-vote-data)
        has-user? (:has-user? updated-vote-data)]

    (if has-user?
      updated-options
      (conj updated-options new-result))))

(defn vote-result-for-active-users [votes active-users]
  (filter (fn [i]
            (some
             #(= % (:user i))
             active-users))
          votes))

