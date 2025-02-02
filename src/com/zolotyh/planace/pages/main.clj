(ns com.zolotyh.planace.pages.main
  (:require
   [com.zolotyh.planace.templates.main :as tpl]
   [com.biffweb :as biff]
   [com.zolotyh.planace.settings :as settings]))

(defn signed-in? [_]
  true)

(defn main [ctx]
  (tpl/main ctx {:header ""
                 :main
                 [:<>
                  [:div {:class "grid grid-cols-2 overflow-y-auto w-4xl mx-auto place-content-around"}
                   [:div {:class "flex flex-col justify-center items-center"}
                    [:h2 {:class "text-6xl font-extralight"} settings/app-name]
                    [:p {:class "text-xl"} settings/slogan]]
                   [:div {:class "p-4"}
                    [:p {:class "text-xl"} "lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante."]]]
                  (biff/form {:action "auth/send-link"})]}))

; (defn home-page [{:keys [recaptcha/site-key params] :as ctx}]
;   (ui/page
;    (assoc ctx ::ui/recaptcha true)
;    (biff/form
;     {:action "/auth/send-link"
;      :id "signup"
;      :hidden {:on-error "/"}}
;     (biff/recaptcha-callback "submitSignup" "signup")
;     [:h2.text-2xl.font-bold (str "Sign up for " settings/app-name)]
;     [:.h-3]
;     [:.flex
;      [:input#email {:name "email"
;                     :type "email"
;                     :autocomplete "email"
;                     :placeholder "Enter your email address"}]
;      [:.w-3]
;      [:button.btn.g-recaptcha
;       (merge (when site-key
;                {:data-sitekey site-key
;                 :data-callback "submitSignup"})
;              {:type "submit"})
;       "Sign up"]]
;     (when-some [error (:error params)]
;       [:<>
;        [:.h-1]
;        [:.text-sm.text-red-600
;         (case error
;           "recaptcha" (str "You failed the recaptcha test. Try again, "
;                            "and make sure you aren't blocking scripts from Google.")
;           "invalid-email" "Invalid email. Try again with a different address."
;           "send-failed" (str "We weren't able to send an email to that address. "
;                              "If the problem persists, try another address.")
;           "There was an error.")]])
;     [:.h-1]
;     [:.text-sm "Already have an account? " [:a.link {:href "/signin"} "Sign in"] "."]
;     [:.h-3]
;     biff/recaptcha-disclosure
;     email-disabled-notice)))
