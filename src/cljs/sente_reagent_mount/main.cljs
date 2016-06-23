(ns sente-reagent-mount.main
  (:require [reagent.core :as reagent]
            [mount.core :as mount]
            [sente-reagent-mount.view :as view]))

(enable-console-print!)

(defonce app-state (reagent/atom {:text "Hello Chestnut!"}))

(defn ^:export main []
  (mount/in-cljc-mode)
  (mount/start)
  (reagent/render [view/main-component] (js/document.getElementById "app")))

