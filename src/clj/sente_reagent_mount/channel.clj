(ns sente-reagent-mount.channel
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (sente-web-server-adapter)]
            [mount.core :refer [defstate]]))

(declare channel)

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :default [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (println "Unknown event: " event " from " session "/" uid)
    (when ?reply-fn
      (?reply-fn {:unknown-event event}))))

(defmethod event-msg-handler :chsk/ws-ping [_])

(defmethod event-msg-handler :chsk/uidport-open [_]
  ; open connection
  (println "Uidport open"))

(defmethod event-msg-handler :chsk/uidport-close [_]
  ; close connection
  (println "Uidport closed"))

(defmethod event-msg-handler :custom/time [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (println "time call from " session "/" uid)
    (swap! (:state channel) inc)
    (when ?reply-fn
      (?reply-fn (System/currentTimeMillis)))))

(defmethod event-msg-handler :custom/call-count [{:keys [?reply-fn]}]
  (when ?reply-fn (?reply-fn @(:state channel))))

(defn init-channel! []
  (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
                connected-uids]} (sente/make-channel-socket! sente-web-server-adapter)]
    {:ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
     :ring-ajax-post-fn ajax-post-fn
     :connected-uids connected-uids
     :ch-recv ch-recv
     :send-fn send-fn
     :router (sente/start-chsk-router! ch-recv event-msg-handler)}))

(defstate channel :start {:channel (init-channel!) :state (atom 0)}
                  :stop  ((:router (:channel channel))))