package fi.pyramus.json.drafts;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.dao.drafts.DraftDAO;
import fi.pyramus.domainmodel.drafts.FormDraft;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class DeleteFormDraftJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    DraftDAO draftDAO = DAOFactory.getInstance().getDraftDAO();

    String url = requestContext.getRequest().getHeader("Referer");
    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    
    FormDraft formDraft = draftDAO.findByUserAndURL(loggedUser, url);
    if (formDraft != null) {
      draftDAO.delete(formDraft);
    } 
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
}
