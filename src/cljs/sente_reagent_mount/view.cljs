(ns sente-reagent-mount.view
  (:require [sente-reagent-mount.channel :as channel]))

(defn send-fn [] (:chsk-send-fn @channel/channel))

(defn check-time [_]
  ((send-fn) [:custom/time {}] 1000
    (fn [rply] (println "Current time: " rply))))

(defn check-call-count [_]
  ((send-fn) [:custom/call-count {}] 1000
    (fn [rply] (println "Call count: " rply))))

(defn main-component []
  [:div
    [:h1 "Sente + Reagent Example"]
    [:div#msg "no message yet"]
    [:div
      [:button {:on-click check-time} "Check time"]
      [:button {:on-click check-call-count} "Check call count"]]])