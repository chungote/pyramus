package fi.pyramus.views.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Manage Time Units view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveTimeUnitsJSONRequestController
 */
public class TimeUnitsViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    
    final EducationalTimeUnit baseTimeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();
    List<EducationalTimeUnit> timeUnits = new ArrayList<EducationalTimeUnit>(educationalTimeUnitDAO.listUnarchived());
    

    Collections.sort(timeUnits, new Comparator<EducationalTimeUnit>() {
      @Override
      public int compare(EducationalTimeUnit o1, EducationalTimeUnit o2) {
        Double units1 = o1.getBaseUnits();
        Double units2 = o2.getBaseUnits();

        if (units1 == units2)
          return 0;
        
        if (o1.equals(baseTimeUnit))
          return -1;
        if (o2.equals(baseTimeUnit))
          return 1;
        
        return units1 > units2 ? 1 : -1;
      }
    });
    
    String jsonTimeUnits = new JSONArrayExtractor("id", "baseUnits", "name").extractString(timeUnits);
    JSONObject joBaseTimeUnit = new JSONObject();
    if (baseTimeUnit != null) {
      joBaseTimeUnit.put("id", baseTimeUnit.getId());
    } else {
      joBaseTimeUnit.put("id", -1);
    }
    
    this.setJsDataVariable(pageRequestContext, "timeUnits", jsonTimeUnits);
    this.setJsDataVariable(pageRequestContext, "baseTimeUnit", joBaseTimeUnit.toString());
    
    pageRequestContext.setIncludeJSP("/templates/settings/timeunits.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
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
    return Messages.getInstance().getText(locale, "settings.timeUnits.pageTitle");
  }

}
