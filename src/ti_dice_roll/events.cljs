(ns ti-dice-roll.events
  (:require
   [re-frame.core :as re-frame]
   [ti-dice-roll.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::update-unit-count
 (fn-traced [db [_ combatant unit count]]
            (assoc-in db [:combatants combatant :units unit] count)))

(re-frame/reg-event-fx
 ::navigate
 (fn-traced [_ [_ handler]]
            {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
            {:db (assoc db :active-panel active-panel)}))
