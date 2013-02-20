package fi.pyramus.json.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.QueryParser;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.help.HelpPageDAO;
import fi.pyramus.domainmodel.help.HelpFolder;
import fi.pyramus.domainmodel.help.HelpPage;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.search.SearchResult;

public class SearchHelpJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    HelpPageDAO helpPageDAO = DAOFactory.getInstance().getHelpPageDAO();
    
    SearchResult<HelpPage> searchResult;

    String text = requestContext.getString("text");
    if (!StringUtils.isBlank(text))
      text = QueryParser.escape(StringUtils.trim(text)) + '*';
    
    searchResult = helpPageDAO.searchHelpPagesBasic(MAX_HELP_PAGES, 0, text);

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

    List<HelpPage> helpPages = searchResult.getResults();
    for (HelpPage helpPage : helpPages) {
      HelpFolder parent = helpPage.getParent();
        if (parent != null) {
        boolean found = false;
        
        for (Map<String, Object> folderInfo : results) {
          if (parent.getId().equals(folderInfo.get("id"))) {
            found = true;
            break;
          }
        }
        
        if (!found) {
          Map<String, Object> folderInfo = new HashMap<String, Object>();
          
          folderInfo.put("id", parent.getId());
          folderInfo.put("type", "folder");
          folderInfo.put("title", parent.getTitleByLocale(requestContext.getRequest().getLocale()).getTitle());
          folderInfo.put("pages", new ArrayList<Map<String, Object>>());
          
          results.add(folderInfo);
        }
      }
    }
    
    for (HelpPage helpPage : helpPages) {
      HelpFolder parent = helpPage.getParent();
      
      if (parent != null) {
        for (Map<String, Object> folderInfo : results) {
          if (parent.getId().equals(folderInfo.get("id"))) {
  
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pages = (List<Map<String, Object>>) folderInfo.get("pages");
            
            Map<String, Object> helpPageInfo = new HashMap<String, Object>();
            helpPageInfo.put("id", helpPage.getId());
            helpPageInfo.put("type", "page");
            helpPageInfo.put("title", helpPage.getTitleByLocale(requestContext.getRequest().getLocale()).getTitle());
            pages.add(helpPageInfo);
            
            break;
          }
        }
      } else {
        Map<String, Object> helpPageInfo = new HashMap<String, Object>();
        helpPageInfo.put("id", helpPage.getId());
        helpPageInfo.put("type", "page");
        helpPageInfo.put("title", helpPage.getTitleByLocale(requestContext.getRequest().getLocale()).getTitle());
        results.add(helpPageInfo);
      }
    }
    
    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  private final static int MAX_HELP_PAGES = 1000;
}
