package fi.pyramus.json.courses;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseComponentDAO;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/** A JSON request controller responsible for archiving course components.
 *
 */
public class ArchiveCourseComponentJSONRequestController extends JSONRequestController {
  
  /** Processes a JSON request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>componentId</code></dt>
   *   <dd>The ID of the component to archive.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(JSONRequestContext requestContext) {
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    Long componentId = requestContext.getLong("componentId");    
    CourseComponent component = componentDAO.findById(componentId);
    componentDAO.archive(component);
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
