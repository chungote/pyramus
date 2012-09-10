package fi.pyramus.dao.courses;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.pyramus.domainmodel.base.CourseBaseVariable;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.CourseBaseVariable_;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalLength;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.Course_;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.persistence.search.SearchTimeFilterMode;

@Stateless
public class CourseDAO extends PyramusEntityDAO<Course> {

  /**
   * Creates a new course into the database.
   * 
   * @param module The module of the course
   * @param name Course name
   * @param subject Course subject
   * @param courseNumber Course number
   * @param beginDate Course begin date
   * @param endDate Course end date
   * @param courseLength Course length
   * @param description Course description
   * @param creatingUser Course owner
   * 
   * @return The created course
   */
  public Course create(Module module, String name, String nameExtension, CourseState state, Subject subject,
      Integer courseNumber, Date beginDate, Date endDate, Double courseLength, EducationalTimeUnit courseLengthTimeUnit, 
      Double distanceTeachingDays, Double localTeachingDays, Double teachingHours, Double planningHours, 
      Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User creatingUser) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());
    EducationalLength educationalLength = new EducationalLength();
    educationalLength.setUnit(courseLengthTimeUnit);
    educationalLength.setUnits(courseLength);

    Course course = new Course();
    course.setModule(module);
    course.setName(name);
    course.setState(state);
    course.setNameExtension(nameExtension);
    course.setDescription(description);
    course.setSubject(subject);
    course.setCourseNumber(courseNumber);
    course.setBeginDate(beginDate);
    course.setEndDate(endDate);
    course.setCourseLength(educationalLength);
    course.setLocalTeachingDays(localTeachingDays);
    course.setDistanceTeachingDays(distanceTeachingDays);
    course.setTeachingHours(teachingHours);    
    course.setPlanningHours(planningHours);
    course.setAssessingHours(assessingHours);
    course.setMaxParticipantCount(maxParticipantCount);
    course.setEnrolmentTimeEnd(enrolmentTimeEnd);
    
    course.setCreator(creatingUser);
    course.setCreated(now);
    course.setLastModifier(creatingUser);
    course.setLastModified(now);

    entityManager.persist(course);

    return course;
  }

  /**
   * Updates a course to the database.
   * 
   * @param course The course to be updated
   * @param name Course name
   * @param subject Course subject
   * @param courseNumber Course number
   * @param beginDate Course begin date
   * @param endDate Course end date
   * @param courseLength Course length 
   * @param courseLengthTimeUnit Course length unit
   * @param description Course description
   * @param user The user making the update, stored as the last modifier of the course
   */
  public void update(Course course, String name, String nameExtension, CourseState courseState, Subject subject,
      Integer courseNumber, Date beginDate, Date endDate, Double courseLength,
      EducationalTimeUnit courseLengthTimeUnit, Double distanceTeachingDays, Double localTeachingDays, Double teachingHours, 
      Double planningHours, Double assessingHours, String description, Long maxParticipantCount, Date enrolmentTimeEnd, User user) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());
    
    EducationalLength educationalLength = course.getCourseLength();
    if (educationalLength == null) {
      educationalLength = new EducationalLength();
    }
    educationalLength.setUnit(courseLengthTimeUnit);
    educationalLength.setUnits(courseLength);

    course.setName(name);
    course.setNameExtension(nameExtension);
    course.setState(courseState);
    course.setDescription(description);
    course.setSubject(subject);
    course.setCourseNumber(courseNumber);
    course.setBeginDate(beginDate);
    course.setEndDate(endDate);
    course.setCourseLength(educationalLength);
    course.setDistanceTeachingDays(distanceTeachingDays);
    course.setLocalTeachingDays(localTeachingDays);
    course.setTeachingHours(teachingHours);
    course.setPlanningHours(planningHours);
    course.setAssessingHours(assessingHours);
    course.setMaxParticipantCount(maxParticipantCount);
    course.setEnrolmentTimeEnd(enrolmentTimeEnd);
    course.setLastModifier(user);
    course.setLastModified(now);

    entityManager.persist(course);
  }
  
  public Course setCourseTags(Course course, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    course.setTags(tags);
    
    entityManager.persist(course);
    
    return course;
  }

  public List<Course> listByCourseVariable(String key, String value) {
    CourseBaseVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getCourseBaseVariableKeyDAO();

    CourseBaseVariableKey courseBaseVariableKey = variableKeyDAO.findByVariableKey(key);
    
//    return (List<Course>) s.createCriteria(CourseBaseVariable.class).add(Restrictions.eq("key", courseBaseVariableKey))
//        .add(Restrictions.eq("value", value)).setProjection(Projections.property("courseBase")).list();
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Course> criteria = criteriaBuilder.createQuery(Course.class);
    Root<CourseBaseVariable> variable = criteria.from(CourseBaseVariable.class);
    Root<Course> course = criteria.from(Course.class);

    criteria.select(course);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(course, variable.get(CourseBaseVariable_.courseBase)),
            criteriaBuilder.equal(course.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(variable.get(CourseBaseVariable_.key), courseBaseVariableKey),
            criteriaBuilder.equal(variable.get(CourseBaseVariable_.value), value)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Course> listByModule(Module module) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Course> criteria = criteriaBuilder.createQuery(Course.class);
    Root<Course> root = criteria.from(Course.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Course_.module), module)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @SuppressWarnings("unchecked")
  public SearchResult<Course> searchCoursesBasic(int resultsPerPage, int page, String text, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "description", text, false);
      addTokenizedSearchCriteria(queryBuilder, "nameExtension", text, false);
      addTokenizedSearchCriteria(queryBuilder, "courseComponents.name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "courseComponents.description", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
      queryBuilder.append(")");
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }
      
      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Course.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedCourse").setParameter("archived", Boolean.FALSE);
      }

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<Course>(page, pages, hits, firstResult, lastResult, query.getResultList());
    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public SearchResult<Course> searchCourses(int resultsPerPage, int page, String name, String tags, String nameExtension,
      String description, CourseState courseState, Subject subject, SearchTimeFilterMode timeFilterMode,
      Date timeframeStart, Date timeframeEnd, boolean filterArchived) {
    return searchCourses(resultsPerPage, page, name, tags, nameExtension, description, courseState, subject, timeFilterMode,
        timeframeStart, timeframeEnd, null, null, filterArchived);
  }
  
  @SuppressWarnings("unchecked")
  public SearchResult<Course> searchCourses(int resultsPerPage, int page, String name, String tags, String nameExtension,
      String description, CourseState courseState, Subject subject, SearchTimeFilterMode timeFilterMode,
      Date timeframeStart, Date timeframeEnd, EducationType educationType, EducationSubtype educationSubtype, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    String timeframeS = null;
    if (timeframeStart != null)
      timeframeS = getSearchFormattedDate(timeframeStart);

    String timeframeE = null;
    if (timeframeEnd != null)
      timeframeE = getSearchFormattedDate(timeframeEnd);

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(name)) {
      addTokenizedSearchCriteria(queryBuilder, "name", name, true);
    }

    if (!StringUtils.isBlank(tags)) {
      addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, true);
    }

    if (!StringUtils.isBlank(nameExtension)) {
      addTokenizedSearchCriteria(queryBuilder, "nameExtension", nameExtension, true);
    }

    if (!StringUtils.isBlank(description)) {
      addTokenizedSearchCriteria(queryBuilder, "description", description, true);
    }
    
    if (courseState != null) {
      addTokenizedSearchCriteria(queryBuilder, "state.id", courseState.getId().toString(), true);
    }

    if (subject != null) {
      addTokenizedSearchCriteria(queryBuilder, "subject.id", subject.getId().toString(), true);
    }

    if (educationType != null)
      addTokenizedSearchCriteria(queryBuilder, "courseEducationTypes.educationType.id", educationType.getId().toString(), true);

    if (educationSubtype != null)
      addTokenizedSearchCriteria(queryBuilder, "courseEducationTypes.courseEducationSubtypes.educationSubtype.id", educationSubtype.getId().toString(), true);
    
    if ((timeframeS != null) && (timeframeE != null)) {
      switch (timeFilterMode) {
      case EXCLUSIVE:
        /** beginDate > timeframeStart and endDate < timeframeEnd **/
        queryBuilder.append(" +(").append("+beginDate:[").append(timeframeS).append(" TO ").append(
            getSearchDateInfinityHigh()).append("]").append("+endDate:[").append(getSearchDateInfinityLow()).append(" TO ")
            .append(timeframeE).append("]").append(")");
        break;
      case INCLUSIVE:
        /**
         * (beginDate between timeframeStart - timeframeEnd or endDate between timeframeStart -
         * timeframeEnd) or (startDate less than timeframeStart and endDate more than
         * timeframeEnd)
         **/
        queryBuilder.append(" +(").append("(").append("beginDate:[").append(timeframeS).append(" TO ").append(
            timeframeE).append("] ").append("endDate:[").append(timeframeS).append(" TO ").append(timeframeE).append(
            "]").append(") OR (").append("beginDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeS).append("] AND ").append("endDate:[").append(timeframeE).append(" TO ").append(
                getSearchDateInfinityHigh()).append("]").append(")").append(")");
        break;
      }
    }
    else if (timeframeS != null) {
      switch (timeFilterMode) {
      case EXCLUSIVE:
        /** beginDate > timeframeStart **/
        queryBuilder.append(" +(").append("+beginDate:[").append(timeframeS).append(" TO ").append(
            getSearchDateInfinityHigh()).append("]").append(")");
        break;
      case INCLUSIVE:
        /** beginDate > timeframeStart or endDate > timeframeStart **/
        queryBuilder.append(" +(").append("beginDate:[").append(timeframeS).append(" TO ").append(
            getSearchDateInfinityHigh()).append("]").append("endDate:[").append(timeframeS).append(" TO ").append(
                getSearchDateInfinityHigh()).append("]").append(")");
        break;
      }
    }
    else if (timeframeE != null) {
      switch (timeFilterMode) {
      case EXCLUSIVE:
        /** endDate < timeframeEnd **/
        queryBuilder.append(" +(").append("+endDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeE).append("]").append(")");
        break;
      case INCLUSIVE:
        /** beginDate < timeframeEnd or endDate < timeframeEnd **/
        queryBuilder.append(" +(").append("beginDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeE).append("] ").append("endDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeE).append("]").append(")");
        break;
      }
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Course.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived)
        query.enableFullTextFilter("ArchivedCourse").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0)
        pages++;

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<Course>(page, pages, hits, firstResult, lastResult, query.getResultList());
    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  
}
