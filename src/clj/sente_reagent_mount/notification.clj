(ns sente-reagent-mount.notification
  (:require [mount.core :refer [defstate]]
            ;[sente-reagent-mount.server :as server]
            [sente-reagent-mount.channel :as channel]))

(defn notify-all []
  (doseq [uid (:any @(:connected-uids channel/channel))]
    ((:send-fn channel/channel) uid [:custom/notification (System/currentTimeMillis)])))

(defn start-notifications []
  (println "Notification thread started")
  (future
    (while true
      (notify-all)
      (Thread/sleep 5000))))

(defn stop-notifications [notification-thread]
  (future-cancel notification-thread)
  (println "Notification thread stopped"))

(defstate notification :start (start-notifications)
                       :stop (stop-notifications notification))