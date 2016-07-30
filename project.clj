(defproject technodrone "0.1.0-SNAPSHOT"
  :description "Data mining and analysis of the tech industry"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.2.0-beta1"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.h2database/h2 "1.3.170"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [org.clojure/core.async "0.2.385"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler technodrone.handler/app}
  :main ^:skip-aot technodrone.core
  :target-path "target/%s"
  :profiles
  {:uberjar {:aot :all}
   :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
