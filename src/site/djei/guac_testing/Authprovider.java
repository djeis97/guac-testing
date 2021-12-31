package site.djei.guac_testing;
import clojure.java.api.Clojure;
import java.util.Map;
import org.apache.guacamole.net.auth.*;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

public class Authprovider extends AbstractAuthenticationProvider {

  public Authprovider() {
    ClassLoader saved = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(
        Authprovider.class.getClassLoader());
    Clojure.var("clojure.core", "require")
        .invoke(Clojure.read("site.djei.guac-testing"));
    Thread.currentThread().setContextClassLoader(saved);
  }
  public String getIdentifier() { return "guac-testing-tutorial"; }
  public AuthenticatedUser authenticateUser(Credentials c) { return null; }

  public UserContext getUserContext(AuthenticatedUser authenticatedUser) {
    return (UserContext)Clojure
      .var("site.djei.guac-testing", "authprovider-getUserContext")
      .invoke(this, authenticatedUser);
  }
}
