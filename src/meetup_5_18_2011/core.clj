(ns meetup-5-18-2011.core
(:use [clojure.contrib.repl-utils]))

;; From Joy of Clojure List 6.3

(defn sort-parts
  "Lazy, tail-recursive, incremental quicksort."
  [work]
  (lazy-seq
   (loop [[part & parts] work]
     (if-let [[pivot & xs] (seq part)]
       (let [smaller? #(< % pivot)]
	 (recur (list* (filter smaller? xs)
		       pivot
		       (remove smaller? xs)
		       parts)))
       (when-let [[x & parts] parts]
	 (cons x
	       (sort-parts parts)))))))

(defn qsort [xs] (sort-parts (list xs)))

;; Deriving let from lambda

((fn [y] (+ y 42)) 10)

(let [y 10] (+ 42 y))

;; Create your own let via macros.

(defmacro my-let [x body]
  (list (list `fn[(first x)]
	      `~body)
	(last x)))

(my-let [z 42] (* z z))

;; if-let, when-let

(let [x false] (if x 'foo 'bar)) ;; bar

(if-let [x false] 'foo 'bar ) ;; bar

(when-let [x true] 'foo) ;; foo

;; often multiple bindings

(let [a 10 b 20] (+ a b)) ;; 30

;; Destructuring

(let [[a b c] (range 1 10)] (+ a b c))
;; 6

(let [[a b c & d] (range 1 10)] d)
;; (4 5 6 7 8 9)

(let [[a b c & d :as all] (range 1 10)] all)
;; (1 2 3 4 5 6 7 8 9)

(def my-name {:first "Rich" :last "Hickey"})

(let [{first :first last :last } my-name]
  (list first last))

(let [{:keys [first last]} my-name]
  (list first last))

;; ("Rich" "Hickey")

;; loop/ recur

(defn pow [num,n]
  (loop [p (dec n) result num]
    (if (zero? p) result
	(recur (dec p) (* result num)))))

(pow 2 3) ;; 8

(pow 2 1000) ;; Doesn't SO

;; in place functions

(macroexpand '#(< % 42))

(fn* [p1__2026#] (< p1__2026# 42))

#(+ %1 %2 42)

;; clojure list API

(seq (.split "1,2,3,4" ","))
;; ("1" "2" "3" "4")

(true? (seq [])) ;;false, nil-punning

(filter #(even? %) (range 0 10))
;; (0 2 4 6 8)

(remove #(even? %) (range 0 10))
;; (1 3 5 7 9)

(list 'Do 'Re 'Mi 'Fa 'Sol 'La 'Ti 'Do)
;; (Do Re Mi Fa Sol La Ti Do)

(list* 'a 'b  (list 'c))
;; (a b c)

(cons 'Do (list 'Re 'Mi))
;; (Do Re Mi)


;; Not holding on to your head from JoC 6.3.3

(let [r (range 1e9)] [(first r) (last r)]) ;=> [0 999999999]

(let [r (range 1e9)] [(last r) (first r)])
