package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseBase;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationType;

@Stateless
public class CourseEducationTypeDAO extends PyramusEntityDAO<CourseEducationType> {

  public CourseEducationType create(CourseBase courseBase, EducationType educationType) {
    EntityManager entityManager = getEntityManager();
    CourseEducationType courseEducationType = new CourseEducationType(educationType);
    entityManager.persist(courseEducationType);
    courseBase.addCourseEducationType(courseEducationType);
    entityManager.persist(courseBase);
    return courseEducationType;
  }

  @Override
  public void delete(CourseEducationType courseEducationType) {
    EntityManager entityManager = getEntityManager();
    CourseBase courseBase = courseEducationType.getCourseBase();
    courseBase.removeCourseEducationType(courseEducationType);
    entityManager.persist(courseBase);
  }

}
