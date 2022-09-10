(ns headphone_brace.core
  (:require [headphone_brace.thing :refer [render-things!]]
  (:gen-class)))


(defn -main
  "render all of the parts and sub parts"
  [& args]
  (render-things!))

