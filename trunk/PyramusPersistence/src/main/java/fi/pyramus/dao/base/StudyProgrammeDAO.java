package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.base.StudyProgramme_;

@Stateless
public class StudyProgrammeDAO extends PyramusEntityDAO<StudyProgramme> {

  public StudyProgramme create(String name, StudyProgrammeCategory category, String code) {
    EntityManager entityManager = getEntityManager();

    StudyProgramme studyProgramme = new StudyProgramme();
    studyProgramme.setName(name);
    studyProgramme.setCategory(category);
    studyProgramme.setCode(code);
    entityManager.persist(studyProgramme);

    return studyProgramme;
  }

  public StudyProgramme findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudyProgramme_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public StudyProgramme findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudyProgramme_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public void update(StudyProgramme studyProgramme, String name, StudyProgrammeCategory category, String code) {
    EntityManager entityManager = getEntityManager();
    studyProgramme.setName(name);
    studyProgramme.setCategory(category);
    studyProgramme.setCode(code);
    entityManager.persist(studyProgramme);
  }

}
