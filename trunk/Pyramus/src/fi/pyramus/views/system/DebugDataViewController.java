package fi.pyramus.views.system;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseStateDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.dao.resources.MaterialResourceDAO;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class DebugDataViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();
    MaterialResourceDAO materialResourceDAO = DAOFactory.getInstance().getMaterialResourceDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    String type = requestContext.getRequest().getParameter("type");
    int count = Integer.parseInt(requestContext.getRequest().getParameter("count"));
    int start = 1;
    String s = requestContext.getRequest().getParameter("start");
    if (!StringUtils.isBlank(s)) {
      start = Integer.parseInt(s);
    }

    User user = userDAO.findById(requestContext.getLoggedUserId());
    
    if ("module".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(new Long(1));
        moduleDAO.create("Moduli " + i, null, null, new Double(10), etu, "Kuvaustekstiä modulille " + i, null, user);
      }
    }
    else if ("course".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(new Long(1));
        CourseState courseState = courseStateDAO.findById(new Long(1));
        courseDAO.create(moduleDAO.findById(new Long(1)), "Kurssi " + i, "", courseState, null, null, null, null, new Double(10), etu, null, null, null, null, null, "Kuvaustekstiä kurssille " + i, null, null, user);
      }
    }
    else if ("resource".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        ResourceCategory resourceCategory = resourceCategoryDAO.findById(new Long(1));
        materialResourceDAO.create("Materiaaliresurssi " + i, resourceCategory, new Double(500));
      }
    }
    else if ("project".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        EducationalTimeUnit etu = educationalTimeUnitDAO.findById(new Long(1));
        projectDAO.create("Projekti " + i, "Kuvaustekstiä projektille " + i, new Double(10), etu, user);
      }
    }
    else if ("student".equals(type)) {
      for (int i = start; i < (start + count); i++) {
        AbstractStudent abstractStudent = abstractStudentDAO.create(new Date(), "030310-123R", Sex.MALE, null, Boolean.FALSE);
        studentDAO.create(abstractStudent, "Etunimi " + i, "Sukunimi " + i, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Boolean.FALSE);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
