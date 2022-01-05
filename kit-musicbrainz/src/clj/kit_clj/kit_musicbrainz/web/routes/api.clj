(ns kit-clj.kit-musicbrainz.web.routes.api
  (:require
    [kit-clj.kit-musicbrainz.web.controllers.health :as health]
    [kit-clj.kit-musicbrainz.web.controllers.artists :as artists]
    [kit-clj.kit-musicbrainz.web.middleware.exception :as exception]
    [kit-clj.kit-musicbrainz.web.middleware.formats :as formats]
    [integrant.core :as ig]
    [reitit.coercion.malli :as malli]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.swagger :as swagger]))

;; Routes
(defn api-routes [base-path]
  [base-path
   ["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title " API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/health"
    {:get health/healthcheck!}]
   ["/v1"
    {}
    ["/artist/:id"
     {:get        {:handler artists/get-by-id}
      :parameters {:path [:map [:id int?]]}}]]])

(defn route-data
  [opts]
  (merge
    opts
    {:coercion   malli/coercion
     :muuntaja   formats/instance
     :swagger    {:id ::api}
     :middleware [;; query-params & form-params
                  parameters/parameters-middleware
                  ;; content-negotiation
                  muuntaja/format-negotiate-middleware
                  ;; encoding response body
                  muuntaja/format-response-middleware
                  ;; exception handling
                  coercion/coerce-exceptions-middleware
                  ;; decoding request body
                  muuntaja/format-request-middleware
                  ;; coercing response bodys
                  coercion/coerce-response-middleware
                  ;; coercing request parameters
                  coercion/coerce-request-middleware
                  ;; exception handling
                  exception/wrap-exception]}))

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  ["" (route-data opts) (api-routes base-path)])