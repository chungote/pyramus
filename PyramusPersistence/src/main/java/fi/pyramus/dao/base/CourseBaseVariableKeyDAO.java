package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey_;

@Stateless
public class CourseBaseVariableKeyDAO extends PyramusEntityDAO<CourseBaseVariableKey> {

  public CourseBaseVariableKey findByVariableKey(String key) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseBaseVariableKey> criteria = criteriaBuilder.createQuery(CourseBaseVariableKey.class);
    Root<CourseBaseVariableKey> root = criteria.from(CourseBaseVariableKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseBaseVariableKey_.variableKey), key)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }


}
