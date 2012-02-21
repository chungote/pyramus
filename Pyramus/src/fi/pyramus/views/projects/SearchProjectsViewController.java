package fi.pyramus.views.projects;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class SearchProjectsViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/projects/searchprojects.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "projects.searchProjects.pageTitle");
  }

}
