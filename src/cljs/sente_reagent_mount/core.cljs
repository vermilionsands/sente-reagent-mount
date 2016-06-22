(ns sente-reagent-mount.core
  (:require [reagent.core :as reagent]
            [mount.core :as mount]
            [sente-reagent-mount.channel :as channel]))

; TODO
; add layout
; split into namespaces
; custom event
; mount start/stop ???
; session/uid

(enable-console-print!)

(defonce app-state (reagent/atom {:text "Hello Chestnut!"}))

(defn socket-ping [evt]
  (println "Ping server")
  (let [send-fn (:chsk-send-fn @channel/channel)]
    (send-fn [:custom/event {:data :custom-ping}])))

(defn socket-callback [evt]
  (println "Ping+callback server")
  (let [send-fn (:chsk-send-fn @channel/channel)]
    (send-fn [:custom/event {:data :custom-call}]
             10000
             (fn [rply] (println "Got a reply: " rply)))))

(defn main-component []
  [:div
   [:h1 "Sente + Reagent Example"]
   [:div#msg "n/a"]
   [:div
    [:button {:on-click socket-ping} "Send message"]
    [:button {:on-click socket-callback} "Send message with callback"]]])

(defn ^:export main []
  (mount/in-cljc-mode)
  (mount/start)
  (reagent/render [main-component] (js/document.getElementById "app")))

