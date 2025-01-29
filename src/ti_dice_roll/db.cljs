(ns ti-dice-roll.db)

(def unit-types
  {:ground #{:infantry
             :mech
             :other}
   :space #{:carrier
            :cruiser
            :destroyer
            :dreadnought
            :fighter
            :flagship
            :warsun
            :other}})

(def unit-combat-defaults
  {:ground {:infantry 8
            :mech 6
            :other 8}
   :space {:carrier 9
           :cruiser 7
           :destroyer 9
           :dreadnought 5
           :fighter 9
           :flagship 5
           :warsun 5
           :other 8}})

(def default-db
  {:custom-dice {:num-dice 0
                 :rolls []}
   :combat-type :space
   :combatants [{:player 1
                 :role :attacker
                 :units {}}
                {:player 2
                 :role :defender
                 :units {}}]
   :combat-rounds []})

;; combat rounds should have this schema:
#_[{:player n
    :units [{:id :fighter
             :rolls [{:value n
                      :hit true}
                     {:value m
                      :hit false}]}
            {:id :cruiser
             :rolls [{:value n
                      :hit false}]}]}
   {:player m
    :units [...]}]

(comment
  (->> (-> :combatants default-db)
       (map :units)
       (map (fn [cunits]
              (map (fn [[unit count]] [unit count]) cunits)))))
