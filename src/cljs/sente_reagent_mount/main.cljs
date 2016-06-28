(ns sente-reagent-mount.main
  (:require [reagent.core :as reagent]
            [mount.core :as mount]
            [sente-reagent-mount.view :as view]))

(enable-console-print!)

(defn ^:export main []
  (mount/in-cljc-mode)
  (mount/start)
  (reagent/render [view/main-component] (js/document.getElementById "app")))

