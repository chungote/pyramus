package fi.pyramus.json.courses;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.pyramus.domainmodel.courses.CourseComponentResource;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class DeleteCourseComponentResourceJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    CourseComponentResourceDAO componentDAO = DAOFactory.getInstance().getCourseComponentResourceDAO();
    Long componentResourceId = requestContext.getLong("courseComponentResourceId");    
    CourseComponentResource componentResource = componentDAO.findById(componentResourceId);
    componentDAO.delete(componentResource);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
