(defproject technodrone "0.1.0"
  :description "Data mining and analysis of the tech industry"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main technodrone.handler
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0-beta1"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.h2database/h2 "1.3.170"]
                 [compojure "1.5.1"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-anti-forgery "1.0.1"]
                 [ring/ring-jetty-adapter "1.6.0-beta4"]
                 [org.clojure/core.async "0.2.385"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler technodrone.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
