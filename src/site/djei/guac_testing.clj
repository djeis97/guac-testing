(ns site.djei.guac-testing
  (:import [org.apache.guacamole.protocol GuacamoleConfiguration]
           [org.apache.guacamole.net.auth AuthenticatedUser AbstractUserContext]
           [org.apache.guacamole.net.auth.permission ObjectPermission ObjectPermission$Type]
           [org.apache.guacamole.net.auth.simple
            SimpleConnection
            SimpleDirectory
            SimpleObjectPermissionSet
            SimpleUser
            SimpleUserContext]))

(def configs
  {"tutorialvncconfig" ["vnc" {"funkyattr" "foo"} {"hostname" "192.168.86.62"}]
   "tutorialsshconfig" ["ssh" {"funkyattr" "bar"} {"hostname" "192.168.86.62"}]})

(defn make-guac-config [protocol parameters]
  (doto (GuacamoleConfiguration.)
    (.setProtocol protocol)
    (.setParameters parameters)))

(defn make-guac-connection [[ident [prot attrs params]]]
  (doto (proxy [SimpleConnection] [ident ident (make-guac-config prot params) true]
          (getAttributes [] attrs))
    (.setParentIdentifier "ROOT")))

(defn make-guac-connections-map [configs]
  (into {} (map (juxt first make-guac-connection)) configs))

;; The real main, provided to us by the stub auth provider class site.djei.guac_testing.Authprovider
(defn authprovider-getUserContext [authprovider ^AuthenticatedUser authenticated-user]
  (let [name (.getIdentifier authenticated-user)
        connection-directory (->> configs make-guac-connections-map SimpleDirectory.)]
    (proxy [AbstractUserContext] []
      (self []
        (let [uc this]
          (proxy [SimpleUser] [name]
            (getConnectionPermissions []
              (-> connection-directory
                  .getIdentifiers
                  (SimpleObjectPermissionSet. [ObjectPermission$Type/READ])))
            (getConnectionGroupPermissions []
              (-> uc
                  .getConnectionGroupDirectory
                  .getIdentifiers
                  (SimpleObjectPermissionSet. [ObjectPermission$Type/READ]))))))
      (getResource [] nil)
      (getAuthenticationProvider [] authprovider)
      (getConnectionDirectory [] connection-directory))))
