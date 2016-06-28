(ns sente-reagent-mount.view
  (:require [sente-reagent-mount.channel :as channel]
            [sente-reagent-mount.state :as state]))

(defn send-fn [] (:chsk-send-fn @channel/channel))

(defn check-time [_]
  ((send-fn) [:custom/time {}]
             1000
             (fn [rply]
               (println "Current time: " rply)
               (state/update-state! "Current time" rply))))

(defn notification-area []
  [:div
   [:h2 "Recent messages from server:"]
   [:div#msg
    [:ul
     (map (fn [{:keys [msg timestamp]}]
            ^{:key (gensym)} [:li (str msg ": " (js/Date. timestamp))]) @state/app-state)]]])

(defn main-component []
  [:div
    [:h1 "Sente + Reagent Example"]
    [notification-area]
    [:div
      [:button {:on-click check-time} "Check time"]]])