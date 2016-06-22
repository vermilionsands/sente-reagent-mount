(ns sente-reagent-mount.channel
  (:require [taoensso.sente :as sente])
  (:require-macros [mount.core :refer [defstate]]))

(enable-console-print!)

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :default  [{:as ev-msg :keys [event]}]
  (println "Unknown event " event))

(defmethod event-msg-handler :chsk/state [{:as ev-msg :keys [?data]}]
  ;(println "chsk/state: " ev-msg))
  (println "chsk/state"))

(defmethod event-msg-handler :chsk/recv [ev-msg]
  ;(println "chsk/recv: " ev-msg))
  (println "chsk/recv"))

(defn init-channel! []
  (let [{:keys [chsk ch-recv send-fn state]} (sente/make-channel-socket! "/chsk" {:type :auto})]
    (println "Init: " send-fn)
    {:chsk chsk
     :chsk-recv ch-recv ; receive channel
     :chsk-send-fn send-fn
     :chsk-state state
     :router (sente/start-client-chsk-router! ch-recv event-msg-handler)}))

(defstate channel :start (init-channel!)
                  :stop ((:router @channel)))


