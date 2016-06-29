(ns sente-reagent-mount.channel
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (sente-web-server-adapter)]
            [mount.core :refer [defstate]]))

(declare channel)

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :default [{:as ev-msg :keys [event id client-id ?data ring-req ?reply-fn send-fn]}]
  ; no session/uid with current config
  (let [session (:session ring-req)
        uid     (:uid session)]
    (println (str client-id " -> unknown event: " event "; " {:session session :uid uid}))
    (when ?reply-fn
      (?reply-fn {:unknown-event event}))))

(defmethod event-msg-handler :chsk/ws-ping [_])

(defmethod event-msg-handler :chsk/uidport-open [{:keys [client-id ring-req]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (println (str client-id " -> uidport open; " {:session session :uid uid}))))

(defmethod event-msg-handler :chsk/uidport-close [{:keys [client-id ring-req]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (println (str client-id " -> uidport closed; " {:session session :uid uid}))))

(defmethod event-msg-handler :custom/time [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (when ?reply-fn
    (?reply-fn (System/currentTimeMillis))))

(defn init-channel! []
  (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
                connected-uids]} (sente/make-channel-socket!
                                           sente-web-server-adapter
                                           {:user-id-fn (fn [ring-req] (:client-id ring-req))})]
    {:ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
     :ring-ajax-post-fn ajax-post-fn
     :connected-uids connected-uids
     :ch-recv ch-recv
     :send-fn send-fn
     :router (sente/start-chsk-router! ch-recv event-msg-handler)}))

(defstate channel :start (init-channel!)
                  :stop  ((:router channel)))