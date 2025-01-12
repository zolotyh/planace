(ns com.zolotyh.planace.poker.utils.url
  (:require
   [reitit.core :as r]))

(defn generate [& args]
  (:path
   (apply  r/match-by-name args)))
