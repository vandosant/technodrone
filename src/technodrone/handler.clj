(ns technodrone.handler
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.anti-forgery :refer :all]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [ring.adapter.jetty :as jetty]
            [technodrone.views :as views])
  (:gen-class))

(cc/defroutes app-routes
  (cc/GET "/" [] (views/home-page))
  (cc/GET "/add-location" [] (views/add-location-page))
  (cc/POST "/add-location" {params :params}
           (views/add-location-results-page params))
  (cc/GET "/location/:loc-id" [loc-id]
          (views/location-page loc-id))
  (cc/GET "/all-locations" []
          (views/all-locations-page))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
    (wrap-defaults site-defaults)
    (wrap-session {:cookie-attrs {:max-age 3600}})))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port  port
                            :join? false})))
