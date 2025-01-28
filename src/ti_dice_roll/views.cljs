(ns ti-dice-roll.views
  (:require
   [clojure.string :as s]
   [re-frame.core :as re-frame]
   [ti-dice-roll.styles :as styles]
   [ti-dice-roll.events :as events]
   [ti-dice-roll.routes :as routes]
   [ti-dice-roll.subs :as subs]))

(defn combat-type-selector []
  [:input :type "radio"])

(defn combatant-display [i combatant]
  [:th {:key (s/join "-" ["combatant" (:player combatant) i])}
   (str "Combatant " (inc i) " (" (:role combatant) ")")])

(defn combat-round [combatants round-i combat-round-roll]
  [:div {:key (str "combat-round-" round-i)}
   [:h2 (str "Round " (inc round-i))]
   [:table
    [:thead
     [:tr
      [:th]
      (map-indexed combatant-display combatants)]]
    [:tbody
     (map (fn [unit]
            [:tr {:key unit}
             [:td unit]
             (map-indexed (fn [i combatant]
                            (let [combatant-unit-roll (get (nth combat-round-roll i) unit)]
                              [:td {:key (s/join "-" ["player" (:player combatant) round-i])}
                               (s/join ", " combatant-unit-roll)]))
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
       (doall
        (map (fn [unit]
               [:tr {:key unit}
                [:td unit]
                (map-indexed (fn [i combatant]
                               [:td {:key (s/join "-" ["player" (:player combatant) unit])}
                                [:input {:type "number"
                                         :value (get (:units combatant) unit)
                                         :on-change #(re-frame/dispatch [::events/update-unit-count i unit (-> % .-target .-value js/parseInt)])}]])
                             @combatants)])
             (-> @combatants first :units keys)))]]
     [:button {:on-click #(re-frame/dispatch [::events/roll-combat-round])} "Roll Combat Round"]

     (reverse (map-indexed #(combat-round @combatants %1 %2) @combat-rounds))]))

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
