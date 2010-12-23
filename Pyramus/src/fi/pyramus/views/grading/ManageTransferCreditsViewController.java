package fi.pyramus.views.grading;

import java.util.List;
import java.util.Locale;

import fi.pyramus.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.BaseDAO;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.GradingDAO;
import fi.pyramus.dao.StudentDAO;
import fi.pyramus.dao.UserDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.UserRole;
import fi.pyramus.views.PyramusViewController;

/**
 * The controller responsible of the Manage Transfer Credits view of the application.
 */
public class ManageTransferCreditsViewController implements PyramusViewController, Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    GradingDAO gradingDAO = DAOFactory.getInstance().getGradingDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    BaseDAO baseDAO = DAOFactory.getInstance().getBaseDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    
    Long studentId = pageRequestContext.getLong("studentId");
    
    Student student = studentDAO.getStudent(studentId);
    List<TransferCredit> transferCredits = gradingDAO.listTransferCreditsByStudent(student);
    List<GradingScale> gradingScales = gradingDAO.listGradingScales();
    List<Subject> subjects = baseDAO.listSubjects();
    List<EducationalTimeUnit> timeUnits = baseDAO.listEducationalTimeUnits();
    List<School> schools = baseDAO.listSchools();
    List<User> users = userDAO.listUsers();
    List<TransferCreditTemplate> transferCreditTemplates = gradingDAO.listTransferCreditTemplates();

    pageRequestContext.getRequest().setAttribute("student", student);
    pageRequestContext.getRequest().setAttribute("transferCredits", transferCredits);
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);
    pageRequestContext.getRequest().setAttribute("subjects", subjects);
    pageRequestContext.getRequest().setAttribute("timeUnits", timeUnits);
    pageRequestContext.getRequest().setAttribute("schools", schools);
    pageRequestContext.getRequest().setAttribute("users", users);
    pageRequestContext.getRequest().setAttribute("transferCreditTemplates", transferCreditTemplates);
    
    pageRequestContext.setIncludeJSP("/templates/grading/managetransfercredits.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
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
    return Messages.getInstance().getText(locale, "grading.manageTransferCredits.pageTitle");
  }

}