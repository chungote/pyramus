package fi.pyramus.binary.studentfiles;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.domainmodel.file.StudentFile;
import fi.pyramus.framework.BinaryRequestController;
import fi.pyramus.framework.UserRole;

/** A binary request controller responsible for serving files
 * attached to students.
 *
 */
public class DownloadStudentFile extends BinaryRequestController {

  /** Processes a binary request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>fileId</code></dt>
   *   <dd>The ID of the student file.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(BinaryRequestContext binaryRequestContext) {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    
    Long fileId = binaryRequestContext.getLong("fileId");
    
    StudentFile studentFile = studentFileDAO.findById(fileId);
    
    if (studentFile != null) {
      binaryRequestContext.setFileName(studentFile.getFileName());
      binaryRequestContext.setResponseContent(studentFile.getData(), studentFile.getContentType());
    }
  }
  
  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
}
