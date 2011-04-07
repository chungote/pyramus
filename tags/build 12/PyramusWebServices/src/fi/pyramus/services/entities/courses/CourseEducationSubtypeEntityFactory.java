package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.services.entities.EntityFactory;

public class CourseEducationSubtypeEntityFactory implements EntityFactory<CourseEducationSubtypeEntity> {

  public CourseEducationSubtypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    CourseEducationSubtype courseEducationSubtype = (CourseEducationSubtype) domainObject;
    return new CourseEducationSubtypeEntity(courseEducationSubtype.getId(), courseEducationSubtype.getEducationSubtype().getName(), 
        courseEducationSubtype.getEducationSubtype().getCode(), courseEducationSubtype.getCourseEducationType().getId());
  }

}
