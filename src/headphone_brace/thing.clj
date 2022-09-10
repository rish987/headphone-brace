(ns headphone_brace.thing
  (:require [scad-clj.model :refer :all]
            [headphone_brace.utils :refer [render!]]))

(def headphone_brace
  (difference
    (cube 100 100 100)
    (union
      (->> (sphere 50)
           (translate [50 0 0])
           (rotate (/ Math/PI 4) [0 1 0]))
      (->> (sphere 50)
           (translate [-50 0 0])
           (rotate (/ Math/PI 4) [0 1 0])))))

(defn eps [len] (+ len 0.1))
(defn eps' [len] (- len 0.1))

(def arc-radius 80)
(def arc-length-interior 160)
(def arc-length 225)
;(def arc-length 20)
(def top-thickness 3.5)
(def thickness 5.8)
(def lip-length 20)
(def lip-thickness 2)
(def width 28.5)
(def extra-width 2)
(def lip-width 1)
(def support-width 0.4)

(def arc-angle-interior (/ (* arc-length-interior 360) (* arc-radius 2 pi)))
(def arc-angle (/ (* arc-length 360) (* arc-radius 2 pi)))
(def lip-angle (/ (* lip-length 360) (* arc-radius 2 pi)))

(defn arc [& args] (apply (partial call-module "arc") args))

(defn base' [radius in-thickness out-thickness height from to] (with-fn 128 (->> 
  (arc from to radius in-thickness out-thickness) 
  (extrude-linear {:height height}
))))

(defn base [radius in-thickness out-thickness height angle] (base' radius in-thickness out-thickness height (- (/ angle 2)) (/ angle 2)))

(defn lip [from to] (union
    (difference 
      (base' arc-radius (+ thickness lip-thickness) top-thickness (+ width (* extra-width 2)) from to)
      (base' arc-radius (eps thickness) 0 (eps width) (eps' from) (eps to))
      (base' arc-radius (eps (+ thickness lip-thickness)) 0 (- width (* lip-width 2)) (eps' from) (eps to))
    )
    (base' (- arc-radius (eps thickness)) support-width 0 (eps (- width (* lip-width 2))) from to)
    (base' (- arc-radius thickness lip-thickness) 0 support-width (eps (- width (* lip-width 2))) from to)
  )
)


(def headphone_brace (union 
  (lip (- (/ arc-angle-interior 2)) (+ (- (/ arc-angle-interior 2)) lip-angle))
  (lip (- (/ arc-angle-interior 2) lip-angle) (/ arc-angle-interior 2))
  (lip (- (/ lip-angle 2)) (/ lip-angle 2))

  (base arc-radius 0 top-thickness (+ width (* extra-width 2)) arc-angle)
))

(defn render-things!
  []
  (render! "headphone_brace" (include "../arc.scad") headphone_brace))

;(render-things!)
