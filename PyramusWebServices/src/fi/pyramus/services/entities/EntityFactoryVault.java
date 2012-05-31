package fi.pyramus.services.entities;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescription;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseUser;
import fi.pyramus.domainmodel.courses.CourseUserRole;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.pyramus.domainmodel.grading.Credit;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.services.entities.base.AcademicTermEntity;
import fi.pyramus.services.entities.base.AcademicTermEntityFactory;
import fi.pyramus.services.entities.base.AddressEntity;
import fi.pyramus.services.entities.base.AddressEntityFactory;
import fi.pyramus.services.entities.base.EducationSubtypeEntity;
import fi.pyramus.services.entities.base.EducationSubtypeEntityFactory;
import fi.pyramus.services.entities.base.EducationTypeEntity;
import fi.pyramus.services.entities.base.EducationTypeEntityFactory;
import fi.pyramus.services.entities.base.EducationalTimeUnitEntity;
import fi.pyramus.services.entities.base.EducationalTimeUnitEntityFactory;
import fi.pyramus.services.entities.base.LanguageEntity;
import fi.pyramus.services.entities.base.LanguageEntityFactory;
import fi.pyramus.services.entities.base.MunicipalityEntity;
import fi.pyramus.services.entities.base.MunicipalityEntityFactory;
import fi.pyramus.services.entities.base.NationalityEntity;
import fi.pyramus.services.entities.base.NationalityEntityFactory;
import fi.pyramus.services.entities.base.SchoolEntity;
import fi.pyramus.services.entities.base.SchoolEntityFactory;
import fi.pyramus.services.entities.base.StudyProgrammeCategoryEntity;
import fi.pyramus.services.entities.base.StudyProgrammeCategoryEntityFactory;
import fi.pyramus.services.entities.base.StudyProgrammeEntity;
import fi.pyramus.services.entities.base.StudyProgrammeEntityFactory;
import fi.pyramus.services.entities.base.SubjectEntity;
import fi.pyramus.services.entities.base.SubjectEntityFactory;
import fi.pyramus.services.entities.courses.CourseComponentEntity;
import fi.pyramus.services.entities.courses.CourseComponentEntityFactory;
import fi.pyramus.services.entities.courses.CourseDescriptionCategoryEntity;
import fi.pyramus.services.entities.courses.CourseDescriptionCategoryEntityFactory;
import fi.pyramus.services.entities.courses.CourseDescriptionEntity;
import fi.pyramus.services.entities.courses.CourseDescriptionEntityFactory;
import fi.pyramus.services.entities.courses.CourseEducationSubtypeEntity;
import fi.pyramus.services.entities.courses.CourseEducationSubtypeEntityFactory;
import fi.pyramus.services.entities.courses.CourseEducationTypeEntity;
import fi.pyramus.services.entities.courses.CourseEducationTypeEntityFactory;
import fi.pyramus.services.entities.courses.CourseEnrolmentTypeEntity;
import fi.pyramus.services.entities.courses.CourseEnrolmentTypeEntityFactory;
import fi.pyramus.services.entities.courses.CourseEntity;
import fi.pyramus.services.entities.courses.CourseEntityFactory;
import fi.pyramus.services.entities.courses.CourseParticipationTypeEntity;
import fi.pyramus.services.entities.courses.CourseParticipationTypeEntityFactory;
import fi.pyramus.services.entities.courses.CourseStudentEntity;
import fi.pyramus.services.entities.courses.CourseStudentEntityFactory;
import fi.pyramus.services.entities.courses.CourseUserEntity;
import fi.pyramus.services.entities.courses.CourseUserEntityFactory;
import fi.pyramus.services.entities.courses.CourseUserRoleEntity;
import fi.pyramus.services.entities.courses.CourseUserRoleEntityFactory;
import fi.pyramus.services.entities.grading.CourseAssessmentEntity;
import fi.pyramus.services.entities.grading.CourseAssessmentEntityFactory;
import fi.pyramus.services.entities.grading.CourseAssessmentRequestEntity;
import fi.pyramus.services.entities.grading.CourseAssessmentRequestEntityFactory;
import fi.pyramus.services.entities.grading.CreditEntity;
import fi.pyramus.services.entities.grading.CreditEntityFactory;
import fi.pyramus.services.entities.grading.GradeEntity;
import fi.pyramus.services.entities.grading.GradeEntityFactory;
import fi.pyramus.services.entities.grading.GradingScaleEntity;
import fi.pyramus.services.entities.grading.GradingScaleEntityFactory;
import fi.pyramus.services.entities.grading.TransferCreditEntity;
import fi.pyramus.services.entities.grading.TransferCreditEntityFactory;
import fi.pyramus.services.entities.modules.ModuleComponentEntity;
import fi.pyramus.services.entities.modules.ModuleComponentEntityFactory;
import fi.pyramus.services.entities.modules.ModuleEntity;
import fi.pyramus.services.entities.modules.ModuleEntityFactory;
import fi.pyramus.services.entities.students.AbstractStudentEntity;
import fi.pyramus.services.entities.students.AbstractStudentEntityFactory;
import fi.pyramus.services.entities.students.StudentEntity;
import fi.pyramus.services.entities.students.StudentEntityFactory;
import fi.pyramus.services.entities.users.UserEntity;
import fi.pyramus.services.entities.users.UserEntityFactory;

public class EntityFactoryVault {
  
  public static TransferCreditEntity buildFromDomainObject(TransferCredit transferCredit) {
    return (TransferCreditEntity) getEntityFactory(TransferCreditEntity.class).buildFromDomainObject(transferCredit);
  }
  
  public static CourseAssessmentEntity buildFromDomainObject(CourseAssessment courseAssessment) {
    return (CourseAssessmentEntity) getEntityFactory(CourseAssessmentEntity.class).buildFromDomainObject(courseAssessment);
  }
  
  public static CreditEntity buildFromDomainObject(Credit credit) {
    return (CreditEntity) getEntityFactory(CreditEntity.class).buildFromDomainObject(credit);
  }
  
  public static GradeEntity buildFromDomainObject(Grade grade) {
    return (GradeEntity) getEntityFactory(GradeEntity.class).buildFromDomainObject(grade);
  }

  public static GradingScaleEntity buildFromDomainObject(GradingScale gradingScale) {
    return (GradingScaleEntity) getEntityFactory(GradingScaleEntity.class).buildFromDomainObject(gradingScale);
  }
  
  public static AbstractStudentEntity buildFromDomainObject(AbstractStudent abstractStudent) {
    return (AbstractStudentEntity) EntityFactoryVault.getEntityFactory(AbstractStudentEntity.class).buildFromDomainObject(abstractStudent);
  }

  public static StudentEntity buildFromDomainObject(Student student) {
    return (StudentEntity) EntityFactoryVault.getEntityFactory(StudentEntity.class).buildFromDomainObject(student);
  }

  public static NationalityEntity buildFromDomainObject(Nationality nationality) {
    return (NationalityEntity) EntityFactoryVault.getEntityFactory(NationalityEntity.class).buildFromDomainObject(nationality);
  }
  
  public static UserEntity buildFromDomainObject(User user) {
    return (UserEntity) EntityFactoryVault.getEntityFactory(UserEntity.class).buildFromDomainObject(user);
  }

  public static AddressEntity buildFromDomainObject(Address address) {
    return (AddressEntity) EntityFactoryVault.getEntityFactory(AddressEntity.class).buildFromDomainObject(address);
  }
  
  public static LanguageEntity buildFromDomainObject(Language language) {
    return (LanguageEntity) EntityFactoryVault.getEntityFactory(LanguageEntity.class).buildFromDomainObject(language);
  }

  public static MunicipalityEntity buildFromDomainObject(Municipality municipality) {
    return (MunicipalityEntity) EntityFactoryVault.getEntityFactory(MunicipalityEntity.class).buildFromDomainObject(municipality);
  }
  
  public static SchoolEntity buildFromDomainObject(School school) {
    return (SchoolEntity) EntityFactoryVault.getEntityFactory(SchoolEntity.class).buildFromDomainObject(school);
  }
  
  public static EducationalTimeUnitEntity buildFromDomainObject(EducationalTimeUnit educationalTimeUnit) {
    return (EducationalTimeUnitEntity) EntityFactoryVault.getEntityFactory(EducationalTimeUnitEntity.class).buildFromDomainObject(educationalTimeUnit);
  }
  
  public static AcademicTermEntity buildFromDomainObject(AcademicTerm academicTerm) {
    return (AcademicTermEntity) EntityFactoryVault.getEntityFactory(AcademicTermEntity.class).buildFromDomainObject(academicTerm);
  }
  
  public static EducationTypeEntity buildFromDomainObject(EducationType educationType) {
    return (EducationTypeEntity) EntityFactoryVault.getEntityFactory(EducationTypeEntity.class).buildFromDomainObject(educationType);
  }
  
  public static EducationSubtypeEntity buildFromDomainObject(EducationSubtype educationSubtype) {
    return (EducationSubtypeEntity) EntityFactoryVault.getEntityFactory(EducationSubtypeEntity.class).buildFromDomainObject(educationSubtype);
  }
  
  public static SubjectEntity buildFromDomainObject(Subject subject) {
    return (SubjectEntity) EntityFactoryVault.getEntityFactory(SubjectEntity.class).buildFromDomainObject(subject);
  }
  
  public static CourseComponentEntity buildFromDomainObject(CourseComponent courseComponent) {
    return (CourseComponentEntity) EntityFactoryVault.getEntityFactory(CourseComponentEntity.class).buildFromDomainObject(courseComponent);
  }
  
  public static CourseEnrolmentTypeEntity buildFromDomainObject(CourseEnrolmentType courseEnrolmentType) {
    return (CourseEnrolmentTypeEntity) EntityFactoryVault.getEntityFactory(CourseEnrolmentTypeEntity.class).buildFromDomainObject(courseEnrolmentType);
  }
  
  public static CourseParticipationTypeEntity buildFromDomainObject(CourseParticipationType courseParticipationType) {
    return (CourseParticipationTypeEntity) EntityFactoryVault.getEntityFactory(CourseParticipationTypeEntity.class).buildFromDomainObject(courseParticipationType);
  }
  
  public static CourseStudentEntity buildFromDomainObject(CourseStudent courseStudent) {
    return (CourseStudentEntity) EntityFactoryVault.getEntityFactory(CourseStudentEntity.class).buildFromDomainObject(courseStudent);
  }
  
  public static CourseUserEntity buildFromDomainObject(CourseUser courseUser) {
    return (CourseUserEntity) EntityFactoryVault.getEntityFactory(CourseUserEntity.class).buildFromDomainObject(courseUser);
  }

  public static CourseUserRoleEntity buildFromDomainObject(CourseUserRole courseUserRole) {
    return (CourseUserRoleEntity) EntityFactoryVault.getEntityFactory(CourseUserRoleEntity.class).buildFromDomainObject(courseUserRole);
  }
  
  public static CourseEducationTypeEntity buildFromDomainObject(CourseEducationType courseEducationType) {
    return (CourseEducationTypeEntity) EntityFactoryVault.getEntityFactory(CourseEducationTypeEntity.class).buildFromDomainObject(courseEducationType);
  }
  
  public static CourseEducationSubtypeEntity buildFromDomainObject(CourseEducationSubtype courseEducationSubtype) {
    return (CourseEducationSubtypeEntity) EntityFactoryVault.getEntityFactory(CourseEducationSubtypeEntity.class).buildFromDomainObject(courseEducationSubtype);
  }
  
  public static CourseEntity buildFromDomainObject(Course course) {
    return (CourseEntity) EntityFactoryVault.getEntityFactory(CourseEntity.class).buildFromDomainObject(course);
  }

  public static CourseDescriptionEntity buildFromDomainObject(CourseDescription courseDescription) {
    return (CourseDescriptionEntity) EntityFactoryVault.getEntityFactory(CourseDescriptionEntity.class).buildFromDomainObject(courseDescription);
  }

  public static CourseDescriptionCategoryEntity buildFromDomainObject(CourseDescriptionCategory category) {
    return (CourseDescriptionCategoryEntity) EntityFactoryVault.getEntityFactory(CourseDescriptionCategoryEntity.class).buildFromDomainObject(category);
  }
  
  public static ModuleEntity buildFromDomainObject(Module module) {
    return (ModuleEntity) EntityFactoryVault.getEntityFactory(ModuleEntity.class).buildFromDomainObject(module);
  }
  
  public static ModuleComponentEntity buildFromDomainObject(ModuleComponent moduleComponent) {
    return (ModuleComponentEntity) EntityFactoryVault.getEntityFactory(ModuleComponentEntity.class).buildFromDomainObject(moduleComponent);
  }

  public static StudyProgrammeEntity buildFromDomainObject(StudyProgramme studyProgramme) {
    return (StudyProgrammeEntity) EntityFactoryVault.getEntityFactory(StudyProgrammeEntity.class).buildFromDomainObject(studyProgramme);
  }

  public static StudyProgrammeCategoryEntity buildFromDomainObject(StudyProgrammeCategory studyProgrammeCategory) {
    return (StudyProgrammeCategoryEntity) EntityFactoryVault.getEntityFactory(StudyProgrammeCategoryEntity.class).buildFromDomainObject(studyProgrammeCategory);
  }

  public static CourseAssessmentRequestEntity buildFromDomainObject(CourseAssessmentRequest courseAssessmentRequest) {
    return (CourseAssessmentRequestEntity) EntityFactoryVault.getEntityFactory(CourseAssessmentRequestEntity.class).buildFromDomainObject(courseAssessmentRequest);
  }
  
  public static Object[] buildFromDomainObjects(Collection<?> entities) {
    Class<?> listClass = null;
    if (!entities.isEmpty()) {
      if (entities instanceof List) {
        listClass = ((List<?>) entities).get(0).getClass();
      } else {
        listClass = entities.iterator().next().getClass();
      }
      
      return buildCollection(getEntityClassForPojoClass(listClass), entities);
    } else {
      return null;  
    }
  }
  
  private static Object[] buildCollection(Class<?> entityClass, Collection<?> objects) {
    Object[] entities = (Object[]) Array.newInstance(entityClass, objects.size());
    int i = 0;
    for (Object domainObject : objects) {
      entities[i++] = getEntityFactory(entityClass).buildFromDomainObject(domainObject);
    }
    
    return entities;
  }
  
  private static EntityFactory<?> getEntityFactory(Class<?> entityClass) {
    return factories.get(entityClass);
  }
  
  private static void registerEntityFactory(Class<?> pojoClass, Class<?> entityClass, EntityFactory<?> entityFactory) {
    factories.put(entityClass, entityFactory);
    pojoEntityClassMap.put(pojoClass, entityClass);
  }
  
  private static Class<?> getEntityClassForPojoClass(Class<?> pojoClass) {
    return pojoEntityClassMap.get(pojoClass);
  }
  
  private static Map<Class<?>, EntityFactory<?>> factories = new HashMap<Class<?>, EntityFactory<?>>();
  private static Map<Class<?>, Class<?>> pojoEntityClassMap = new HashMap<Class<?>, Class<?>>();

  static {
    /* Base */ 
    
    registerEntityFactory(Address.class, AddressEntity.class, new AddressEntityFactory());
    registerEntityFactory(School.class, SchoolEntity.class, new SchoolEntityFactory());
    registerEntityFactory(Nationality.class, NationalityEntity.class, new NationalityEntityFactory());
    registerEntityFactory(School.class, SchoolEntity.class, new SchoolEntityFactory());
    registerEntityFactory(Nationality.class, NationalityEntity.class, new NationalityEntityFactory());
    registerEntityFactory(Municipality.class, MunicipalityEntity.class, new MunicipalityEntityFactory());
    registerEntityFactory(Language.class, LanguageEntity.class, new LanguageEntityFactory());
    registerEntityFactory(EducationalTimeUnit.class, EducationalTimeUnitEntity.class, new EducationalTimeUnitEntityFactory());
    registerEntityFactory(AcademicTerm.class, AcademicTermEntity.class, new AcademicTermEntityFactory());
    registerEntityFactory(EducationType.class, EducationTypeEntity.class, new EducationTypeEntityFactory());
    registerEntityFactory(EducationSubtype.class, EducationSubtypeEntity.class, new EducationSubtypeEntityFactory());
    registerEntityFactory(Subject.class, SubjectEntity.class, new SubjectEntityFactory());
    registerEntityFactory(StudyProgramme.class, StudyProgrammeEntity.class, new StudyProgrammeEntityFactory());
    registerEntityFactory(StudyProgrammeCategory.class, StudyProgrammeCategoryEntity.class, new StudyProgrammeCategoryEntityFactory());
    
    /* Users */ 
    
    registerEntityFactory(User.class, UserEntity.class, new UserEntityFactory());
    
    /* Students */ 
    
    registerEntityFactory(AbstractStudent.class ,AbstractStudentEntity.class, new AbstractStudentEntityFactory());
    registerEntityFactory(Student.class, StudentEntity.class, new StudentEntityFactory());
    
    /* Courses */
        
    registerEntityFactory(Course.class, CourseEntity.class, new CourseEntityFactory());
    registerEntityFactory(CourseComponent.class, CourseComponentEntity.class, new CourseComponentEntityFactory());
    registerEntityFactory(CourseEnrolmentType.class, CourseEnrolmentTypeEntity.class, new CourseEnrolmentTypeEntityFactory());
    registerEntityFactory(CourseParticipationType.class, CourseParticipationTypeEntity.class, new CourseParticipationTypeEntityFactory());
    registerEntityFactory(CourseStudent.class, CourseStudentEntity.class, new CourseStudentEntityFactory());
    registerEntityFactory(CourseEducationType.class, CourseEducationTypeEntity.class, new CourseEducationTypeEntityFactory());
    registerEntityFactory(CourseEducationSubtype.class, CourseEducationSubtypeEntity.class, new CourseEducationSubtypeEntityFactory());
    registerEntityFactory(CourseUser.class, CourseUserEntity.class, new CourseUserEntityFactory());
    registerEntityFactory(CourseUserRole.class, CourseUserRoleEntity.class, new CourseUserRoleEntityFactory());
    registerEntityFactory(CourseDescriptionCategory.class, CourseDescriptionCategoryEntity.class, new CourseDescriptionCategoryEntityFactory());
    registerEntityFactory(CourseDescription.class, CourseDescriptionEntity.class, new CourseDescriptionEntityFactory());
    
    /* Grading */
    
    registerEntityFactory(CourseAssessment.class, CourseAssessmentEntity.class, new CourseAssessmentEntityFactory());
    registerEntityFactory(Credit.class, CreditEntity.class, new CreditEntityFactory());
    registerEntityFactory(Grade.class, GradeEntity.class, new GradeEntityFactory());
    registerEntityFactory(GradingScale.class, GradingScaleEntity.class, new GradingScaleEntityFactory());
    registerEntityFactory(TransferCredit.class, TransferCreditEntity.class, new TransferCreditEntityFactory());
    registerEntityFactory(CourseAssessmentRequest.class, CourseAssessmentRequestEntity.class, new CourseAssessmentRequestEntityFactory());
    
    /* Modules */
  
    registerEntityFactory(Module.class, ModuleEntity.class, new ModuleEntityFactory());
    registerEntityFactory(ModuleComponent.class, ModuleComponentEntity.class, new ModuleComponentEntityFactory());
  }
}
