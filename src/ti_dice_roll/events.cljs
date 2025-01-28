(ns ti-dice-roll.events
  (:require
   [re-frame.core :as re-frame]
   [ti-dice-roll.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn roll-d10 []
  (inc (rand-int 10)))

(defn roll-d10s [x]
  (repeatedly x roll-d10))

(defn roll-for-unit [u]
  (roll-d10s (last u)))

(defn roll-for-combatant [combatant]
  (reduce (partial apply assoc)
          {}
          (map #(vector (first %) (roll-for-unit %)) (:units combatant))))

(re-frame/reg-cofx
 ::roll-combat
 (fn [{:keys [db] :as cofx} _]
   (let [combatants (:combatants db)]
     (assoc cofx :combat-rolls (map roll-for-combatant combatants)))))

(re-frame/reg-cofx
 ::roll-custom-dice
 (fn [{:keys [db] :as cofx} _]
   (let [num-custom-dice (get-in db [:custom-dice :num-dice] 0)]
     (assoc cofx :custom-dice-roll (roll-d10s num-custom-dice)))))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::update-custom-dice-count
 (fn-traced [db [_ new-dice-count]]
            (assoc-in db [:custom-dice :num-dice] new-dice-count)))

(re-frame/reg-event-fx
 ::roll-custom-dice
 [(re-frame/inject-cofx ::roll-custom-dice)]
 (fn-traced [{:keys [db custom-dice-roll]} [_]]
            {:db (assoc-in db [:custom-dice :rolls]
                           (conj (get-in db [:custom-dice :rolls] [])
                                 custom-dice-roll))}))

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

(re-frame/reg-event-fx
 ::roll-combat-round
 [(re-frame/inject-cofx ::roll-combat)]
 (fn-traced [{:keys [db combat-rolls]} [_]]
            {:db (assoc db :combat-rounds (conj (:combat-rounds db) combat-rolls))}))
(comment
  (roll-d10s 3)
  (roll-for-combatant {:units {:fighter 3 :dread 2}}))
