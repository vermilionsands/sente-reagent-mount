(ns sente-reagent-2.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [sente-reagent-2.core-test]))

(enable-console-print!)

(doo-tests 'sente-reagent-2.core-test)
