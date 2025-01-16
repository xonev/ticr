(ns ti-dice-roll.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [ti-dice-roll.events :as events]
   [ti-dice-roll.routes :as routes]
   [ti-dice-roll.views :as views]
   [ti-dice-roll.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
