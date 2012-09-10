package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveSubjectsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("subjectsTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "subjectsTable." + i;
      Long subjectId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".subjectId"));
      String code = jsonRequestContext.getRequest().getParameter(colPrefix + ".code");
      String name = jsonRequestContext.getRequest().getParameter(colPrefix + ".name");
      Long educationTypeId = jsonRequestContext.getLong(colPrefix + ".educationTypeId");
      EducationType educationType = null;
      if (educationTypeId != null)
        educationType = educationTypeDAO.findById(educationTypeId);
      boolean modified = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter(colPrefix + ".modified")) == 1;
      
      if (subjectId == -1) {
        subjectDAO.create(code, name, educationType); 
      }
      else if (modified) {
        Subject subject = subjectDAO.findById(subjectId);
        subjectDAO.update(subject, code, name, educationType);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
