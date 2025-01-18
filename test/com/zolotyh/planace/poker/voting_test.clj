(ns com.zolotyh.planace.poker.voting-test
  (:require
   [clojure.test :refer [deftest is]]
   [com.zolotyh.planace.poker.voting :as voting]))

(deftest empty-vote-list
  (is (=
       [{:user "foo" :val "bar"}]
       (voting/vote {:user "foo" :val "bar"} [])))

  "Should add new vote if vote-list is empty")

(deftest add-vote-to-list
  (is (=
       [{:user "old-user" :val "old-value"}
        {:user "new-user" :val "new-value"}]

       (voting/vote
        {:user "new-user" :val "new-value"}
        [{:user "old-user" :val "old-value"}])))

  "Should append result if user is not found in list")

(deftest add-vote
  (is (=
       [{:user "old-user" :val "new-value"}]

       (voting/vote
        {:user "old-user" :val "new-value"}
        [{:user "old-user" :val "old-value"}])))

  "Should update vote of user")

(deftest keep-active-users
  (is (= [{:user 1}]
         (voting/vote-result-for-active-users
          [{:user 1}]
          [1])))
  "Should keep votes for active users")

(deftest filter-active-users
  (is (= [{:user 1}]
         (voting/vote-result-for-active-users
          [{:user 1} {:user 2}]
          [1])))
  "Should filter votes for active users")

(deftest vote-by-user-id
  (is (= {:user 1}
         (voting/vote-by-user-id
          [{:user 1} {:user 2}]
          1)))
  "Should return vote by user-id from vote list")

(deftest update-sequence-by-active-vote
  (is (= [{:val 1}
          {:val 2 :active true}]

         (voting/update-sequence-by-active-vote
          [{:val 1} {:val 2}]
          {:val 2})))
  "Should update sequence using active-vote")

(deftest vote-result-for-active-users
  (is (= [{:val 1}
          {:val 2 :active true}]

         (voting/vote-result-for-active-users
          [{:val 1} {:val 2}]
          {:val 2})))
  "Should update sequence using active-vote")
