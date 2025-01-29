(ns ti-dice-roll.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::combat-type
 (fn [db]
   (:combat-type db)))

(re-frame/reg-sub
 ::custom-dice
 (fn [db]
   (:custom-dice db)))

(re-frame/reg-sub
 ::combatants
 (fn [db]
   (:combatants db)))

(re-frame/reg-sub
 ::combat-rounds
 (fn [db]
   (:combat-rounds db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
