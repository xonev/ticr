(ns ti-dice-roll.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [ti-dice-roll.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
