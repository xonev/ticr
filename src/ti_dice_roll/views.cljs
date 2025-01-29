(ns ti-dice-roll.views
  (:require
   [clojure.string :as s]
   [re-frame.core :as re-frame]
   [ti-dice-roll.styles :as styles]
   [ti-dice-roll.db :as db]
   [ti-dice-roll.events :as events]
   [ti-dice-roll.routes :as routes]
   [ti-dice-roll.subs :as subs]))

(defn combat-type-selector []
  [:input :type "radio"])

(defn combatant-name [i combatant]
  (str "Combatant " (inc i) " (" (:role combatant) ")"))

(defn combatant-key [i combatant]
  (s/join "-" ["combatant" (:player combatant) i]))

(defn combatant-display [i combatant]
  [:th {:key (combatant-key i combatant)}
   (combatant-name i combatant)])

(defn combat-round [combatants round-i combat-round-roll]
  [:div {:key (str "combat-round-" round-i)}
   [:h2 (str "Round " (inc round-i))]
   (map-indexed (fn [i combatant]
                  (let [key (s/join "-" ["combatant" i])
                        combatant-round-rolls (nth combat-round-roll i)]
                    (seq [[:h3 {:key (combatant-key i combatant)} (combatant-name i combatant)]
                          [:table {:key (str "rolls-" (combatant-key i combatant))}
                           [:thead
                            [:tr [:th {:key "0"}]
                             (map (fn [unit-rolls]
                                    (let [[unit-id rolls] unit-rolls]
                                      [:th {:key unit-id} unit-id]))
                                  combatant-round-rolls)]]
                           [:tbody
                            [:tr
                             [:td "Rolls"]
                             (map (fn [unit-rolls]
                                    (let [[unit-id rolls] unit-rolls]
                                      [:td {:key unit-id} (s/join "," rolls)]))
                                  combatant-round-rolls)]]]])))
                combatants)])

;; home

(defn home-panel []
  (let [combat-type (re-frame/subscribe [::subs/combat-type])
        combatants (re-frame/subscribe [::subs/combatants])
        combat-rounds (re-frame/subscribe [::subs/combat-rounds])
        custom-dice (re-frame/subscribe [::subs/custom-dice])]
    [:div
     [:a {:on-click #(re-frame/dispatch [::events/navigate :about])
          :href ""}
      "About"]
     [:h1
      {:class (styles/level1)}
      "Custom Roller"]
     [:div
      [:label {:for "custom-dice-count"} "Number of dice"]
      [:input {:type "number"
               :id "custom-dice-count"
               :value (get @custom-dice :num-dice 0)
               :on-change #(re-frame/dispatch [::events/update-custom-dice-count (-> % .-target .-value js/parseInt)])}]
      [:button {:on-click #(re-frame/dispatch [::events/roll-custom-dice])} "Roll!"]
      (let [rolls (get @custom-dice :rolls)]
        (if (> (count rolls) 0)
          [:div
           [:h2 "Roll result"]
           [:div
            (s/join ", " (last rolls))]]))]
     [:h1
      {:class (styles/level1)}
      (str "Combat Roller")]
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
                                         :value (get (:units combatant) unit 0)
                                         :on-change #(re-frame/dispatch [::events/update-unit-count i unit (-> % .-target .-value js/parseInt)])}]])
                             @combatants)])
             (get db/unit-types @combat-type)))]]
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
