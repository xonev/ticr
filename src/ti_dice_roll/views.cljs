(ns ti-dice-roll.views
  (:require
   [clojure.string :as s]
   [re-frame.core :as re-frame]
   [ti-dice-roll.styles :as styles]
   [ti-dice-roll.events :as events]
   [ti-dice-roll.routes :as routes]
   [ti-dice-roll.subs :as subs]))

(defn combatant-display [i combatant]
  [:th (str "Combatant " (inc i) " (" (:role combatant) ")")])

(defn combat-round [combatants i combat-round-roll]
  [:div
   [:h2 (str "Round " (inc i))]
   [:table
    [:thead
     [:tr
      [:th]
      (map-indexed combatant-display combatants)]]
    [:tbody
     (map (fn [unit]
            [:tr
             [:td unit]
             (map-indexed (fn [i _]
                            [:td (s/join ", " (get (nth combat-round-roll i) unit))])
                          combatants)])
          (-> combatants first :units keys))]]])

;; home

(defn home-panel []
  (let [combatants (re-frame/subscribe [::subs/combatants])
        combat-rounds (re-frame/subscribe [::subs/combat-rounds])]
    [:div
     [:h1
      {:class (styles/level1)}
      (str "Combat Roller")]
     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])
           :href ""}
       "About"]]
     [:table
      [:thead
       [:tr
        [:th]
        (map-indexed combatant-display @combatants)]]
      [:tbody
       (map (fn [unit]
              [:tr
               [:td unit]
               (map-indexed (fn [i combatant]
                              [:td [:input {:type "number"
                                            :value (get (:units combatant) unit)
                                            :on-change #(re-frame/dispatch [::events/update-unit-count i unit (-> % .-target .-value js/parseInt)])}]])
                            @combatants)])
            (-> @combatants first :units keys))]]
     [:button {:on-click #(re-frame/dispatch [::events/roll-combat-round])} "Roll Combat Round"]

     (map-indexed #(combat-round @combatants %1 %2) @combat-rounds)]))

(defmethod routes/panels :home-panel [] [home-panel])

;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:on-click #(re-frame/dispatch [::events/navigate :home])
         :href ""}
     "go to Home Page"]]])

(defmethod routes/panels :about-panel [] [about-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))

(comment
  (nth '({:fighters '(1 2 3)} {:fighters '(4 5 6)}) 0))
