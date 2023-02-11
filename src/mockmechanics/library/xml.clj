(ns mockmechanics.library.xml
  (:require [mockmechanics.library.util :refer :all]))

(load "xml-jvm")

(defn get-children [map child-name]
  (filter (fn [child]
            (= (:tag child) child-name))
          (:content map)))

(defn get-child [map child-name]
  (first (get-children map child-name)))

(defn has-map-signature? [map tag-name attrs]
  (and
   (= (:tag map) tag-name)
   (every? (fn [[name value]]
             (= (get-in map [:attrs name]) value))
           attrs)))

(defn get-child-if [map tag attrs]
  (find-if (slots has-map-signature? _ tag attrs) (:content map)))

(defn tree->xml [t]
  (let [[name content] t]
    {:tag name
     :attrs nil
     :content (if (vector? content)
                (vec (map tree->xml (partition 2 content)))
                [content])}))
