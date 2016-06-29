(ns sente-reagent-mount.state
  (:require [reagent.core :as reagent]))

(defonce app-state (reagent/atom #queue []))

(defn update-state! [msg t]
  (swap! app-state (fn [state v]
                     (let [state (conj state v)]
                       (if (> (count state) 10)
                         (pop state)
                         state)))
         {:msg msg :timestamp t}))