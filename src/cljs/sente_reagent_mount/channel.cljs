(ns sente-reagent-mount.channel
  (:require [taoensso.sente :as sente]
            [sente-reagent-mount.state :as state])
  (:require-macros [mount.core :refer [defstate]]))

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :default [{:as ev-msg :keys [event ?data]}])

(defmethod event-msg-handler :chsk/recv [{:as ev-msg :keys [?data]}]
  (let [[push-key push-data] ?data]
    (condp = push-key
      :custom/notification (state/update-state! "Time notification" push-data))))

(defn init-channel! []
  (let [{:keys [chsk ch-recv send-fn state]} (sente/make-channel-socket! "/chsk" {:type :auto})]
    {:chsk chsk
     :chsk-recv ch-recv ; receive channel
     :chsk-send-fn send-fn
     :chsk-state state
     :router (sente/start-client-chsk-router! ch-recv event-msg-handler)}))

(defstate channel :start (init-channel!)
                  :stop ((:router @channel)))


