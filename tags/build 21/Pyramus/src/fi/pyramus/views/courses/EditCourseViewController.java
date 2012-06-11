package fi.pyramus.views.courses;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.pyramus.PageRequestContext;
import fi.pyramus.UserRole;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.BaseDAO;
import fi.pyramus.dao.CourseDAO;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.StudentDAO;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseUser;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.util.StringAttributeComparator;
import fi.pyramus.views.PyramusViewController;

/**
 * The controller responsible of the Edit Course view of the application.
 * 
 * @see fi.pyramus.json.users.CreateGradingScaleJSONRequestController
 */
public class EditCourseViewController implements PyramusViewController, Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    BaseDAO baseDAO = DAOFactory.getInstance().getBaseDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    // The course to be edited
    
    Course course = courseDAO.getCourse(NumberUtils.createLong(pageRequestContext.getRequest().getParameter("course")));
    pageRequestContext.getRequest().setAttribute("course", course);
    
    // Create a hashmap of the education types and education subtypes selected in the course
    List<EducationType> educationTypes = baseDAO.listEducationTypes();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    pageRequestContext.getRequest().setAttribute("educationTypes", educationTypes);
    Map<String, Boolean> enabledEducationTypes = new HashMap<String, Boolean>();
    for (CourseEducationType courseEducationType : course.getCourseEducationTypes()) {
      for (CourseEducationSubtype courseEducationSubtype : courseEducationType.getCourseEducationSubtypes()) {
        enabledEducationTypes.put(courseEducationType.getEducationType().getId() + "."
            + courseEducationSubtype.getEducationSubtype().getId(), Boolean.TRUE);
      }
    }
    pageRequestContext.getRequest().setAttribute("enabledEducationTypes", enabledEducationTypes);
    
    // Various lists of base entities from module, course, and resource DAOs 

    List<CourseStudent> courseStudents = courseDAO.listCourseStudentsByCourse(course);
    Collections.sort(courseStudents, new Comparator<CourseStudent>() {
      @Override
      public int compare(CourseStudent o1, CourseStudent o2) {
        int cmp = o1.getStudent().getLastName().compareToIgnoreCase(o2.getStudent().getLastName());
        if (cmp == 0)
          cmp = o1.getStudent().getFirstName().compareToIgnoreCase(o2.getStudent().getFirstName());
        return cmp;
      }
    });
    
    List<CourseUser> courseUsers = courseDAO.listCourseUsers(course);
    Collections.sort(courseUsers, new Comparator<CourseUser>() {
      @Override
      public int compare(CourseUser o1, CourseUser o2) {
        int cmp = o1.getUser().getLastName().compareToIgnoreCase(o2.getUser().getLastName());
        if (cmp == 0)
          cmp = o1.getUser().getFirstName().compareToIgnoreCase(o2.getUser().getFirstName());
        return cmp;
      }
    });
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = course.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    List<CourseComponent> courseComponents = courseDAO.listCourseComponents(course);
    
    // course students students
    
    Map<Long, List<Student>> courseStudentsStudents = new HashMap<Long, List<Student>>();
    
    for (CourseStudent courseStudent : courseStudents) {
      courseStudentsStudents.put(courseStudent.getId(), studentDAO.listStudentsByAbstractStudent(courseStudent.getStudent().getAbstractStudent()));
    }
    
    // Subjects
    Map<Long, List<Subject>> subjectsByEducationType = new HashMap<Long, List<Subject>>();
    List<Subject> subjectsByNoEducationType = baseDAO.listSubjectsByEducationType(null);
    Collections.sort(subjectsByNoEducationType, new StringAttributeComparator("getName"));
    for (EducationType educationType : educationTypes) {
      List<Subject> subjectsOfType = baseDAO.listSubjectsByEducationType(educationType);
      if ((subjectsOfType != null) && (subjectsOfType.size() > 0)) {
        Collections.sort(subjectsOfType, new StringAttributeComparator("getName"));
        subjectsByEducationType.put(educationType.getId(), subjectsOfType);
      }
    }
    
    List<EducationalTimeUnit> educationalTimeUnits = baseDAO.listEducationalTimeUnits();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("states", courseDAO.listCourseStates());
    pageRequestContext.getRequest().setAttribute("roles", courseDAO.listCourseUserRoles());
    pageRequestContext.getRequest().setAttribute("subjectsByNoEducationType", subjectsByNoEducationType);
    pageRequestContext.getRequest().setAttribute("subjectsByEducationType", subjectsByEducationType);
    pageRequestContext.getRequest().setAttribute("courseParticipationTypes", courseDAO.listCourseParticipationTypes());
    pageRequestContext.getRequest().setAttribute("courseEnrolmentTypes",courseDAO.listCourseEnrolmentTypes());
    pageRequestContext.getRequest().setAttribute("courseStudents", courseStudents);
    pageRequestContext.getRequest().setAttribute("courseUsers", courseUsers);
    pageRequestContext.getRequest().setAttribute("courseLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("courseComponents", courseComponents);
    pageRequestContext.getRequest().setAttribute("courseStudentsStudents", courseStudentsStudents);
    pageRequestContext.getRequest().setAttribute("courseDescriptions", courseDAO.listCourseDescriptionsByCourseBase(course));
    pageRequestContext.getRequest().setAttribute("courseDescriptionCategories", courseDAO.listCourseDescriptionCategories());
    
    pageRequestContext.setIncludeJSP("/templates/courses/editcourse.jsp");
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
    return Messages.getInstance().getText(locale, "courses.editCourse.breadcrumb");
  }

}