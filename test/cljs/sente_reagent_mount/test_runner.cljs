(ns sente-reagent-mount.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [sente-reagent-mount.core-test]))

(enable-console-print!)

(doo-tests 'sente-reagent-mount.core-test)
