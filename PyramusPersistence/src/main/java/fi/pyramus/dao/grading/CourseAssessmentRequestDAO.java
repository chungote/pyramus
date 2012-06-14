package fi.pyramus.dao.grading;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseStudent_;
import fi.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.pyramus.domainmodel.grading.CourseAssessmentRequest_;
import fi.pyramus.domainmodel.students.Student;

@Stateless
public class CourseAssessmentRequestDAO extends PyramusEntityDAO<CourseAssessmentRequest> {

  public CourseAssessmentRequest create(CourseStudent courseStudent, Date created, String requestText) {
    EntityManager entityManager = getEntityManager();

    CourseAssessmentRequest courseAssessmentRequest = new CourseAssessmentRequest();

    courseAssessmentRequest.setCourseStudent(courseStudent);
    courseAssessmentRequest.setCreated(created);
    courseAssessmentRequest.setRequestText(requestText);
    
    entityManager.persist(courseAssessmentRequest);
    
    return courseAssessmentRequest;
  }
  
  /**
   * Lists all student's course assessments excluding archived ones
   * 
   * @return list of all students course assessments
   */
  public List<CourseAssessmentRequest> listByCourseStudent(CourseStudent courseStudent) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.courseStudent), courseStudent),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  public CourseAssessmentRequest update(CourseAssessmentRequest courseAssessmentRequest, Date created, String requestText) {
    EntityManager entityManager = getEntityManager();

    courseAssessmentRequest.setCreated(created);
    courseAssessmentRequest.setRequestText(requestText);
    
    entityManager.persist(courseAssessmentRequest);
    
    return courseAssessmentRequest;
  }

  public List<CourseAssessmentRequest> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudent = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseAssessmentRequest> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudent = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
