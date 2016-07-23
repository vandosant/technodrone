(defproject technodrone "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.2.0-beta1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.2.385"]]
  :main ^:skip-aot technodrone.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
