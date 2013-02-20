package fi.pyramus.views.help;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class ViewHelpViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/help/viewhelp.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "help.viewHelp.pageTitle");
  }

}
