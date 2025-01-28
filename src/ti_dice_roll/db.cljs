(ns ti-dice-roll.db)

(def unit-types
  {:ground #{:infantry
             :mech}
   :space #{:carrier
            :cruiser
            :destroyer
            :dreadnought
            :fighter
            :flagship
            :warsun}})

(def default-db
  {:combatants [{:player 1
                 :role :attacker
                 :units {:fighters 5
                         :carriers 1
                         :dreadnoughts 0
                         :cruisers 0}}
                {:player 2
                 :role :defender
                 :units {:fighters 5
                         :carriers 1
                         :dreadnoughts 1
                         :cruisers 0}}]
   :combat-rounds []})

(comment
  (->> (-> :combatants default-db)
       (map :units)
       (map (fn [cunits]
              (map (fn [[unit count]] [unit count]) cunits)))))
