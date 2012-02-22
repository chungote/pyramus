package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Municipality_;

@Stateless
public class MunicipalityDAO extends PyramusEntityDAO<Municipality> {

  public void create(String name, String code) {
    EntityManager entityManager = getEntityManager();

    Municipality municipality = new Municipality();
    municipality.setName(name);
    municipality.setCode(code);

    entityManager.persist(municipality);
  }

  /**
   * Returns the municipality corresponding to the given code.
   * 
   * @param code
   *          The municipality code
   * 
   * @return The municipality corresponding to the given code
   */
  public Municipality findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Municipality> criteria = criteriaBuilder.createQuery(Municipality.class);
    Root<Municipality> root = criteria.from(Municipality.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Municipality_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public Municipality update(Municipality municipality, String name, String code) {
    EntityManager entityManager = getEntityManager();
    municipality.setName(name);
    municipality.setCode(code);
    entityManager.persist(municipality);
    return municipality;
  }

}
