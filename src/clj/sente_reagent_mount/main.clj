(ns sente-reagent-mount.main
  (:require [mount.core :as mount]
            [sente-reagent-mount.server]
            [sente-reagent-mount.notification]))

(defn -main [& [port]]
  (mount/start-with-args {:port port}))
