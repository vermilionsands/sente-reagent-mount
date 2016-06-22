(ns sente-reagent-mount.main
  (:require [mount.core :as mount]
            [sente-reagent-mount.server]))

(defn -main [& [port]]
  (mount/start-with-args {:port port}))
