package fi.pyramus.breadcrumbs;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class BreadcrumbHandler {
  
  public void clear() {
    breadcrumbs.clear();
  }
  
  public boolean contains(HttpServletRequest request) {
    return indexOf(getBreadcrumbUrl(request, true)) >= 0;
  }
  
  public void process(HttpServletRequest request, Breadcrumbable breadcrumbable) {
    String shortUrl = getBreadcrumbUrl(request, false);
    String completeUrl = getBreadcrumbUrl(request, true);
    String lastUrl = getSize() == 0 ? "" : breadcrumbs.get(breadcrumbs.size() - 1).getUrl();
    if (lastUrl.startsWith(shortUrl)) {
      pop();
      Breadcrumb breadcrumb = new Breadcrumb(completeUrl, breadcrumbable.getName(request.getLocale()));
      breadcrumbs.add(breadcrumb);
    }
    else {
      int index = indexOf(completeUrl);
      if (index == -1) {
        Breadcrumb breadcrumb = new Breadcrumb(completeUrl, breadcrumbable.getName(request.getLocale()));
        breadcrumbs.add(breadcrumb);
      }
      else {
        prune(index);
      }
    }
  }
  
  private int indexOf(String url) {
    for (int i = 0;  i < breadcrumbs.size(); i++) {
      if (breadcrumbs.get(i).getUrl().equals(url)) {
        return i;
      }
    }
    return -1;
  }
  
  private boolean isBreadcrumbParameter(String s) {
    return "resetbreadcrumb".equals(s);
  }
  
  public void pop() {
    if (getSize() > 0) {
      breadcrumbs.remove(breadcrumbs.size() - 1);
    }
  }
  
  private void prune(int index) {
    while (breadcrumbs.size() - 1 > index) {
      breadcrumbs.remove(breadcrumbs.size() - 1);
    }
  }
  
  public int getSize() {
    return breadcrumbs.size();
  }
  
  public List<Breadcrumb> getBreadcrumbs() {
    return breadcrumbs;
  }
  
  public String getBreadcrumbUrl(HttpServletRequest request, boolean includeRequestParams) {
    StringBuilder sb = new StringBuilder();
    sb.append(request.getRequestURL());
    if (includeRequestParams) {
      boolean firstParam = true;
      Enumeration<String> params = request.getParameterNames();
      while (params.hasMoreElements()) {
        String param = params.nextElement();
        if (!isBreadcrumbParameter(param)) {
          sb.append(firstParam ? '?' : '&'); 
          sb.append(param);
          sb.append("=");
          sb.append(request.getParameter(param));
          firstParam = false;
        }
      }
    }
    return sb.toString();
  }
  
  private List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();

}