(ns com.zolotyh.planace.poker.utils.http)

(defn is-htmx [{:keys [headers]}]
  (= (get "hx-request" headers) nil))

(defn htmx-status [ctx]
  (if (is-htmx ctx)
    200 303))
