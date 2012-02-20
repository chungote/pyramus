package fi.pyramus.plugin.auth;

import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.domainmodel.users.User;

/**
 * Defines requirements for a class capable of authorizing using external authentication source. 
 * (e.g. OpenId) 
 */
public interface ExternalAuthenticationProvider extends AuthenticationProvider {
  public void performDiscovery(RequestContext requestContext);
  public User processResponse(RequestContext requestContext) throws AuthenticationException;
}
