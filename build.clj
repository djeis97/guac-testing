(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b] ; for b/git-count-revs
            [org.corfield.build :as bb]))

(def lib 'site.djei/guac-testing)
#_(def version "0.1.0-SNAPSHOT")
;; alternatively, use MAJOR.MINOR.COMMITS:
(def version (format "0.0.%s" (b/git-count-revs nil)))

(defn rebuild-classes [opts]
  (b/delete {:path "classes"})
  (let [comp-basis (b/create-basis {:aliases [:guac]})]
    (b/javac {:basis comp-basis
              :src-dirs ["src"]
              :class-dir "classes"})
    (b/compile-clj {:ns-compile ['site.djei.guac-testing]
                    :basis comp-basis
                    :src-dirs ["src"]
                    :class-dir "classes"})))

(defn uber [opts]
  (b/delete {:path "target"})
  (rebuild-classes opts)
  (b/copy-dir {:src-dirs ["src" "resources" "classes"] :target-dir "target/classes"})
  (let [basis (b/create-basis {})]
    (b/write-pom {:basis basis
                  :class-dir "target/classes"
                  :lib 'site.djei/guac-testing
                  :version "STANDALONE"})
    (b/uber {:basis basis
             :class-dir "target/classes"
             :conflict-handlers org.corfield.log4j2-conflict-handler/log4j2-conflict-handler
             :uber-file "target/guac-testing.jar"})))
