(ns com.zolotyh.planace-test
  ;; If you add more test files, require them here so that they'll get loaded by com.zolotyh.planace/on-save
  (:require
   [clojure.test :refer [deftest is testing]]
   [com.zolotyh.planace.model :refer [add-user-vote create-new-vote]]))

(deftest vote-test
  (testing "then pass uuid of user and vote then it should be set"
    (let [uuid #uuid "90b41082-be77-411b-8a51-a45d825d3f04"
          vote  {:vote/result [{:vote 4 :user uuid}]}
          result (add-user-vote vote uuid 100)]
      (is (= (get-in result [:vote/result 0 :vote]) 100)))))

(deftest create-vote-test
  (testing "should create new vote from params and room metadata"
    (let [uuid #uuid "b2c69ecf-c899-4b55-908e-46e7ebb8d056"
          room-uuid  #uuid "3c777afc-5335-4438-8e9f-316977d07495"
          room {:xt/id room-uuid :room/members [uuid]}
          vote {:vote/title "test-title"}
          vote-item {:user uuid}]
      (is
       (=
        (create-new-vote room vote)
        {:vote/title "test-title"
         :vote/room room-uuid
         :vote/result [vote-item]})))))
