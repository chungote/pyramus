package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class CourseEducationTypeEntityFactory implements EntityFactory<CourseEducationTypeEntity> {

  public CourseEducationTypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    CourseEducationType educationType = (CourseEducationType) domainObject; 
    CourseEducationSubtypeEntity[] subtypes = (CourseEducationSubtypeEntity[]) EntityFactoryVault.buildFromDomainObjects(educationType.getCourseEducationSubtypes());
    return new CourseEducationTypeEntity(educationType.getId(), educationType.getEducationType().getName(), educationType.getEducationType().getCode(), subtypes);
  }

}
