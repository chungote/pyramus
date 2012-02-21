package fi.pyramus.views.courses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Create Course view of the application.
 * 
 * @see fi.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class CoursePlannerViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    List<CourseBean> courseBeans = new ArrayList<CoursePlannerViewController.CourseBean>();
    for (Course course : courseDAO.listUnarchived()) {
      courseBeans.add(new CourseBean(course));
    }
    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    pageRequestContext.getRequest().setAttribute("courseBeans", courseBeans);
    
    pageRequestContext.setIncludeJSP("/templates/courses/courseplanner.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating courses is available for users with
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
    return Messages.getInstance().getText(locale, "courses.coursePlanner.pageTitle");
  }

  public class CourseBean {
    
    public CourseBean(Course course) {
      this.course = course;
    }
    
    public String getCourseName() {
      if (StringUtils.isBlank(course.getNameExtension()))
        return course.getName();
      else 
        return course.getName() + " (" + course.getNameExtension() + ")";
    }
    
    public Set<Long> getEducationTypes() {
      Set<Long> result = new HashSet<Long>();
      
      List<CourseEducationType> courseEducationTypes = course.getCourseEducationTypes();
      for (CourseEducationType courseEducationType : courseEducationTypes) {
        result.add(courseEducationType.getEducationType().getId());
      }
      
      return result;
    }
    
    public Set<Long> getEducationSubtypes() {
      Set<Long> result = new HashSet<Long>();
      
      List<CourseEducationType> courseEducationTypes = course.getCourseEducationTypes();
      for (CourseEducationType courseEducationType : courseEducationTypes) {
        for (CourseEducationSubtype courseEducationSubtype : courseEducationType.getCourseEducationSubtypes()) {
          result.add(courseEducationSubtype.getEducationSubtype().getId());
        }
      }
      
      return result;
    }
    
    public Course getCourse() {
      return course;
    }
    
    private Course course;
  }
}
