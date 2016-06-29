(ns sente-reagent-mount.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]]
            [mount.core :refer [defstate] :as mount]
            [sente-reagent-mount.channel :as channel])
  (:gen-class))

(defn main-page []
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (io/input-stream (io/resource "public/index.html"))})

(defroutes routes
  (GET  "/" _ (main-page))
  (GET  "/chsk" req ((:ring-ajax-get-or-ws-handshake channel/channel) req))
  (POST "/chsk" req ((:ring-ajax-post-fn channel/channel) req))
  (resources "/"))


(def http-handler
  (-> routes
      (wrap-defaults (merge api-defaults {:session true}))
      wrap-with-logger
      wrap-gzip))

;; move to config
(defn start-server [port]
  (let [port (Integer. (or port 10555))
        server (run-server http-handler {:port port :join? false})]
    (println "Http-Kit server started on port:" port)
    server))

(defn stop-server [server]
  (server :timeout 100)
  (println "Http-Kit server stopped"))

(defstate server :start (start-server (:port mount/args))
                 :stop (stop-server server))