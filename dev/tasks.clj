(ns tasks
  (:require [clojure.test :refer [run-all-tests]]
            [com.biffweb.tasks :as tasks]))

(defn t "run all unit test" [] (run-all-tests))

;; Tasks should be vars (#'hello instead of hello) so that `clj -M:dev help`
;; can
;; print their docstrings.
(def custom-tasks {"custom-task" #'t})

(def tasks (merge tasks/tasks custom-tasks))
