package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.Nationality_;

@Stateless
public class NationalityDAO extends PyramusEntityDAO<Nationality> {

  /**
   * Returns the nationality corresponding to the given code.
   * 
   * @param code
   *          The nationality code
   * 
   * @return The nationality corresponding to the given code
   */
  public Nationality findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Nationality> criteria = criteriaBuilder.createQuery(Nationality.class);
    Root<Nationality> root = criteria.from(Nationality.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Nationality_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
