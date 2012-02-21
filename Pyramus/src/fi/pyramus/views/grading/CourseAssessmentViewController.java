package fi.pyramus.views.grading;

import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Course Grading view of the application.
 */
public class CourseAssessmentViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    
    Long courseStudentId = pageRequestContext.getLong("courseStudentId");
    
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    
    CourseAssessment assessment = courseAssessmentDAO.findByCourseStudent(courseStudent);
    
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();

    pageRequestContext.getRequest().setAttribute("courseStudent", courseStudent);
    pageRequestContext.getRequest().setAttribute("assessment", assessment);
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);
    
    pageRequestContext.setIncludeJSP("/templates/grading/courseassessment.jsp");
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
    return Messages.getInstance().getText(locale, "grading.courseAssessment.pageTitle");
  }

}
