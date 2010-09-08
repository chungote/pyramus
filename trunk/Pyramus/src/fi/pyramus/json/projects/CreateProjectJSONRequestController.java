package fi.pyramus.json.projects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.pyramus.JSONRequestContext;
import fi.pyramus.dao.BaseDAO;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.ModuleDAO;
import fi.pyramus.dao.ProjectDAO;
import fi.pyramus.dao.UserDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.UserRole;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.json.JSONRequestController;
import fi.pyramus.persistence.usertypes.ProjectModuleOptionality;

public class CreateProjectJSONRequestController implements JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    BaseDAO baseDAO = DAOFactory.getInstance().getBaseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    // Project

    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    User loggedUser = userDAO.getUser(jsonRequestContext.getLoggedUserId());
    Long optionalStudiesLengthTimeUnitId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(
        "optionalStudiesLengthTimeUnit"));
    EducationalTimeUnit optionalStudiesLengthTimeUnit = baseDAO
        .getEducationalTimeUnit(optionalStudiesLengthTimeUnitId);
    Double optionalStudiesLength = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter(
        "optionalStudiesLength"));
    Project project = projectDAO.createProject(name, description, optionalStudiesLength,
        optionalStudiesLengthTimeUnit, loggedUser);

    // Project modules

    int rowCount = NumberUtils.createInteger(
        jsonRequestContext.getRequest().getParameter("modulesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "modulesTable." + i;
      Long moduleId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".moduleId"));
      Module module = moduleDAO.getModule(moduleId);
      int optionality = new Integer(jsonRequestContext.getRequest().getParameter(colPrefix + ".optionality"))
          .intValue();
      projectDAO.createProjectModule(project, module, ProjectModuleOptionality.getOptionality(optionality));
    }
    
    String redirectURL = jsonRequestContext.getRequest().getContextPath() + "/projects/editproject.page?project=" + project.getId();
    String refererAnchor = jsonRequestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    jsonRequestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
