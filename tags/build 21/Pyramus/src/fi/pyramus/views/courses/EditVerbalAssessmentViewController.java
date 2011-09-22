package fi.pyramus.views.courses;

import java.util.Locale;

import fi.pyramus.PageRequestContext;
import fi.pyramus.UserRole;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.GradingDAO;
import fi.pyramus.domainmodel.grading.Credit;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.views.PyramusViewController;

/**
 * The controller responsible of the Edit Course view of the application.
 * 
 * @see fi.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class EditVerbalAssessmentViewController implements PyramusViewController, Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    GradingDAO gradingDAO = DAOFactory.getInstance().getGradingDAO();
    
    Long creditId = pageRequestContext.getLong("creditId");
    Credit credit = creditId != null ? gradingDAO.findCreditById(creditId) : null;

    if (credit != null)
      pageRequestContext.getRequest().setAttribute("verbalAssessment", credit.getVerbalAssessment());
      
    pageRequestContext.setIncludeJSP("/templates/courses/editverbalassessment.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing courses is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "courses.editVerbalAssessmentDialog.dialogTitle");
  }

}
