(ns ti-dice-roll.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::combatants
 (fn [db]
   (:combatants db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
