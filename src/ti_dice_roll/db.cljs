(ns ti-dice-roll.db)

(def default-db
  {:combatants [{:role :attacker
                 :units {:fighters 5
                         :carriers 1
                         :dreadnoughts 0}}
                {:role :defender
                 :units {:fighters 5
                         :carriers 1
                         :dreadnoughts 1}}]
   :combat-rounds []})

(comment
  (->> (-> :combatants default-db)
       (map :units)
       (map (fn [cunits]
              (map (fn [[unit count]] [unit count]) cunits)))))
