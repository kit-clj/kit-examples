(ns kit.guestbook.web.routes.pages
  (:require
   [kit.guestbook.web.controllers.guestbook :as guestbook]
   [kit.guestbook.web.middleware.exception :as exception]
   [kit.guestbook.web.pages.layout :as layout]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]))

(defn wrap-page-defaults []
  (let [error-page (layout/error-page
                    {:status 403
                     :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page})))

(defn home [request]
  (layout/render request "home.html" {:messages (guestbook/get-messages request)}))

(defn error [request]
  (layout/render request "error.html" {}))

;; Routes
(defn page-routes [base-path]
  [base-path
   ["/" {:get home}]
   ["/error" {:get error}]
   ["/save-message" {:post guestbook/save-message!}]])

(defn route-data
  [{:keys [template-opts]}]
  {:swagger    {:id ::api}
   :middleware [;; Default middleware for pages
                (wrap-page-defaults)
                ;; query-params & form-params
                parameters/parameters-middleware
                ;; encoding response body
                muuntaja/format-response-middleware
                ;; exception handling
                exception/wrap-exception]})

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-selmer!)
  ["" (route-data opts) (page-routes base-path)])
