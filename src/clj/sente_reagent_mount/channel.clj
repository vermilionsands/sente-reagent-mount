(ns sente-reagent-mount.channel
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (sente-web-server-adapter)]
            [mount.core :refer [defstate]]))

; TODO
; add callback on open connection
; add periodic notification

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

(defmethod event-msg-handler :custom/event [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (println "Custom event: " event " from " session "/" uid)
    (when ?reply-fn
      (?reply-fn {:echo "echo"}))))

(defn init-channel! []
  (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
                connected-uids]} (sente/make-channel-socket! sente-web-server-adapter)]
    {:ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
     :ring-ajax-post-fn ajax-post-fn
     :connected-uids connected-uids
     :ch-recv ch-recv
     :send-fn send-fn
     :router (sente/start-chsk-router! ch-recv event-msg-handler)}))

; declare + dodac atom, jako mapa map?
(defstate channel :start (init-channel!)
                  :stop  ((:router channel)))