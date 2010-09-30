package fi.pyramus.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.lucene.queryParser.QueryParser;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.sun.enterprise.container.common.impl.EntityManagerWrapper;

public class PyramusDAO {
  
  protected EntityManager getEntityManager() {
    try {
      InitialContext initialContext = new InitialContext();
      return (EntityManager) initialContext.lookup("java:comp/env/persistence/pyramusEntityManager");
    } catch (NamingException e) {
      throw new PersistenceException(e);
    }
  }
  
  protected Session getHibernateSession() {
    EntityManagerWrapper entityManagerWrapper = (EntityManagerWrapper) getEntityManager();
    return ((EntityManagerImpl) entityManagerWrapper.getDelegate()).getSession();
  }

  protected void forceReindex(Object o) {
    FullTextSession fullTextSession = Search.getFullTextSession(getHibernateSession());
    fullTextSession.index(o);
  }
  
  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, String fieldName, String value, boolean required, boolean escapeSpecialChars) {
    String inputText = value.replaceAll(" +", " ");
    String[] tokens = (escapeSpecialChars ? QueryParser.escape(inputText) : inputText).split("[ ,]");
    for (String token : tokens) {
      queryBuilder.append(' ');
      if (required)
        queryBuilder.append("+");

      queryBuilder.append(fieldName).append(':').append(token);
    }
  }
  
  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, String fieldName1, String fieldName2, String value, boolean required, boolean escapeSpecialChars) {
    String inputText = value.replaceAll(" +", " ");
    String[] tokens = (escapeSpecialChars ? QueryParser.escape(inputText) : inputText).split("[ ,]");
    for (String token : tokens) {
      if (required)
        queryBuilder.append("+");

      queryBuilder.append('(').append(fieldName1).append(':').append(token).append(' ').append(fieldName2).append(':').append(token).append(')');
    }
  }
  
  protected String getSearchDateInfinityHigh() {
    return DATERANGE_INFINITY_HIGH;
  }
  
  protected String getSearchDateInfinityLow() {
    return DATERANGE_INFINITY_LOW;
  }
  
  protected String getSearchFormattedDate(Date date) {
    return COURSE_SEARCH_DATE_FORMAT.format(date);
  }
  
  private static final String DATERANGE_INFINITY_LOW = "00000000";
  private static final String DATERANGE_INFINITY_HIGH = "99999999";
  private static final DateFormat COURSE_SEARCH_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
}