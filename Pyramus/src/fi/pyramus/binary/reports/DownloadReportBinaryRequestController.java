package fi.pyramus.binary.reports;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.internetix.smvc.controllers.BinaryRequestController;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.UserRole;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.reports.Report;

public class DownloadReportBinaryRequestController implements BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();

    Long reportId = binaryRequestContext.getLong("reportId");
    String formatParameter = binaryRequestContext.getString("format");
    ReportOutputFormat outputFormat = Enum.valueOf(ReportOutputFormat.class, formatParameter);
    
    StringBuilder magicKeyBuilder = new StringBuilder()
      .append(Long.toHexString(reportId))
      .append('-')
      .append(Long.toHexString(System.currentTimeMillis()))
      .append('-')
      .append(Long.toHexString(Thread.currentThread().getId()));
  
    MagicKey magicKey = magicKeyDAO.create(magicKeyBuilder.toString()); 
    
    Report report = reportDAO.findById(reportId);
    
    String reportName = report.getName().toLowerCase().replaceAll("[^a-z0-9\\.]", "_");
    String reportsContextPath = System.getProperty("reports.contextPath");
    
    StringBuilder urlBuilder = new StringBuilder()
      .append(reportsContextPath)
      .append("/preview")
      .append("?magicKey=")
      .append(magicKey.getName())
      .append("&__report=reports/")
      .append(reportId)
      .append(".rptdesign")
      .append("&__format=").append(outputFormat.name());
    
    Map<String, String[]> parameterMap = binaryRequestContext.getRequest().getParameterMap();
    for (String parameterName : parameterMap.keySet()) {
      if (!reservedParameters.contains(parameterName)) {
        String[] values = parameterMap.get(parameterName);
        for (String value : values) {
          // TODO ISO-8859-1 should be UTF-8, once Birt's parameter dialog form has its accept-charset="UTF-8" set 
          try {
            urlBuilder.append('&').append(parameterName).append('=').append(URLEncoder.encode(value, "ISO-8859-1"));
          }
          catch (UnsupportedEncodingException e) {
            throw new SmvcRuntimeException(e);
          }
        }
      }
    }
    
    binaryRequestContext.setFileName(reportName + '.' + outputFormat.getExt());
    binaryRequestContext.setContentUrl(urlBuilder.toString());
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
  private enum ReportOutputFormat {
    HTML ("html"),
    PDF  ("pdf"),
    RTF  ("rtf"),
    DOC  ("doc"),
    XLS  ("xml");
    
    ReportOutputFormat (String ext) {
      this.ext = ext;
    }
    
    public String getExt() {
      return ext;
    }
    
    private String ext;
  }
  

  private static Set<String> reservedParameters = new HashSet<String>();
  
  static {
    reservedParameters.add("reportId");
    reservedParameters.add("magicKey");
    reservedParameters.add("format");
    reservedParameters.add("__format");
    reservedParameters.add("__report");
  }

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    // TODO Auto-generated method stub
    throw new RuntimeException("Not implemented");
  }
}
