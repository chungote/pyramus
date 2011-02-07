package fi.pyramus.services.entities.grading;

import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.services.entities.EntityFactory;

public class GradingScaleEntityFactory implements EntityFactory<GradingScaleEntity> {

  public GradingScaleEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    GradingScale gradingScale = (GradingScale) domainObject;
    return new GradingScaleEntity(gradingScale.getId(), gradingScale.getName(), gradingScale.getDescription(), gradingScale.getArchived());
  }

}
