(ns com.zolotyh.planace.templates.utils
  (:require
   [clojure.java.io :as io]
   [ring.util.response :as ring-response]))

(defn static-path [path]
  (if-some [last-modified (some-> (io/resource (str "public" path))
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str path "?t=" last-modified)
    path))
