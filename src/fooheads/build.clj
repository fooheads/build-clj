(ns fooheads.build
  "from https://kozieiev.com/blog/packaging-clojure-into-jar-uberjar-with-tools-build/"
  (:require
    [clojure.edn :as edn]
    [clojure.tools.build.api :as b]
    [deps-deploy.deps-deploy :as dd]))


(def build-folder "target")
(def jar-content (str build-folder "/classes"))
(def basis (b/create-basis {:project "deps.edn"}))


(defn lib-data []
  (edn/read-string (slurp "lib.edn")))


(defn jar-file-name [lib-name version]
  (format "%s/%s-%s.jar" build-folder (name lib-name) version))


(defn clean
  [_]
  (b/delete {:path build-folder})
  (println (format "Build folder \"%s\" removed" build-folder)))


(defn jar
  [_]
  (let [{lib-name :name version :version} (lib-data)
        jar-file-name (jar-file-name lib-name version)]
    (clean nil)

    (b/copy-dir {:src-dirs   ["src" "resources"]
                 :target-dir jar-content})

    (b/write-pom {:class-dir jar-content
                  :lib       lib-name
                  :version   version
                  :basis     basis
                  :src-dirs  ["src"]})

    (b/jar {:class-dir jar-content
            :jar-file  jar-file-name lib-name version})
    (println (format "Jar file created: \"%s\"" jar-file-name))))


(defn deploy
  [_]
  (let [{lib-name :name} (lib-data)]
    (jar nil)

    (dd/deploy {:installer :remote
                :artifact jar-file-name
                :pom-file (b/pom-path {:lib lib-name
                                       :class-dir jar-content})})))


(comment
  (clean "")
  (jar "")
  (deploy ""))

