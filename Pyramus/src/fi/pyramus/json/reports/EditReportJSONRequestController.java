package fi.pyramus.json.reports;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportCategoryDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportCategory;
import fi.pyramus.UserRole;
import fi.pyramus.JSONRequestController;

/**
 * The controller responsible of editing a report. 
 * 
 * @see fi.pyramus.views.reports.EditReportViewController
 */
public class EditReportJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a report.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    ReportCategoryDAO categoryDAO = DAOFactory.getInstance().getReportCategoryDAO();

    Long reportId = requestContext.getLong("reportId");
    Report report = reportDAO.findById(reportId);

    Long reportCategoryId = requestContext.getLong("category");
    ReportCategory category = reportCategoryId == null ? null : categoryDAO.findById(reportCategoryId);
    
    String name = requestContext.getString("name");
    
    reportDAO.update(report, name, category);

    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
