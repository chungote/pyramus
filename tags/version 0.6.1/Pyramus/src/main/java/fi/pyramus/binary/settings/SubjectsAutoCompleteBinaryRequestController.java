package fi.pyramus.binary.settings;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.QueryParser;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.framework.BinaryRequestController;
import fi.pyramus.framework.UserRole;

public class SubjectsAutoCompleteBinaryRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    String text = binaryRequestContext.getString("text");

    StringBuilder resultBuilder = new StringBuilder();
    resultBuilder.append("<ul>");

    if (!StringUtils.isBlank(text)) {
      text = QueryParser.escape(StringUtils.trim(text)) + '*';
      
      List<Subject> subjects = subjectDAO.searchSubjectsBasic(100, 0, text).getResults();
      
      for (Subject subject : subjects) {
        addSubject(resultBuilder, subject, binaryRequestContext.getRequest().getLocale());
      }
    }
    
    resultBuilder.append("</ul>");

    try {
      binaryRequestContext.setResponseContent(resultBuilder.toString().getBytes("UTF-8"), "text/html;charset=UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
  private void addSubject(StringBuilder resultBuilder, Subject subject, Locale locale) {
    resultBuilder
      .append("<li>")
      .append("<span>")
      .append(StringEscapeUtils.escapeHtml(getSubjectText(subject, locale)))
      .append("</span>")
      .append("<input type=\"hidden\" name=\"id\" value=\"")
      .append(subject.getId())
      .append("\"/>")
      .append("</li>");
  }
  
  private String getSubjectText(Subject subject, Locale locale) {
    if (subject == null)
      return null;
    
    String subjectName = subject.getName();
    String subjectCode = subject.getCode();
    String subjectEducationType = subject.getEducationType() != null ? subject.getEducationType().getName() : null;
    
    String localizedSubject = subjectName;
    
    if ((subjectCode != null) && (subjectEducationType != null)) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterWithEducationType", new Object[] {
        subjectCode,
        subjectName,
        subjectEducationType
      });
    } else if (subjectEducationType != null) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterNoSubjectCode", new Object[] {
        subjectName,
        subjectEducationType
      });
    } else if (subjectCode != null) {
      localizedSubject = Messages.getInstance().getText(locale, 
          "generic.subjectFormatterNoEducationType", new Object[] {
        subjectCode,
        subjectName
      });
    }

    return localizedSubject;
  }
  
}