package fi.pyramus.json.modules;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.pyramus.dao.base.CourseEducationTypeDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.dao.courses.CourseDescriptionDAO;
import fi.pyramus.dao.modules.ModuleComponentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.CourseDescription;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing module. 
 * 
 * @see fi.pyramus.views.modules.EditModuleViewController
 */
public class EditModuleJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDescriptionDAO courseDescriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseEducationTypeDAO courseEducationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    CourseEducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getCourseEducationSubtypeDAO();
    ModuleComponentDAO moduleComponentDAO = DAOFactory.getInstance().getModuleComponentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    Long moduleId = requestContext.getLong("moduleId");
    
    Module module = moduleDAO.findById(moduleId);

    Long version = requestContext.getLong("version");
    if (!module.getVersion().equals(version))
      throw new StaleObjectStateException(Module.class.getName(), module.getId());
    
    // Education types and subtypes submitted from the web page

    Map<Long, Vector<Long>> chosenEducationTypes = new HashMap<Long, Vector<Long>>();
    Enumeration<String> parameterNames = requestContext.getRequest().getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String name = (String) parameterNames.nextElement();
      if (name.startsWith("educationType.")) {
        String[] nameElements = name.split("\\.");
        Long educationTypeId = new Long(nameElements[1]);
        Long educationSubtypeId = new Long(nameElements[2]);
        Vector<Long> v = chosenEducationTypes.containsKey(educationTypeId) ? chosenEducationTypes.get(educationTypeId)
            : new Vector<Long>();
        v.add(educationSubtypeId);
        if (!chosenEducationTypes.containsKey(educationTypeId)) {
          chosenEducationTypes.put(educationTypeId, v);
        }
      }
    }

    // Course Descriptions
    
    List<CourseDescriptionCategory> descriptionCategories = descriptionCategoryDAO.listUnarchived();
    Set<CourseDescription> nonExistingDescriptions = new HashSet<CourseDescription>();
    
    for (CourseDescriptionCategory cat: descriptionCategories) {
      String varName = "courseDescription." + cat.getId().toString();
      Long descriptionCatId = requestContext.getLong(varName + ".catId");
      String descriptionText = requestContext.getString(varName + ".text");

      CourseDescription oldDesc = courseDescriptionDAO.findByCourseAndCategory(module, cat);

      if ((descriptionCatId != null) && (descriptionCatId.intValue() != -1)) {
        // Description has been submitted from form 
        if (oldDesc != null)
          courseDescriptionDAO.update(oldDesc, module, cat, descriptionText);
        else
          courseDescriptionDAO.create(module, cat, descriptionText);
      } else {
        // Description wasn't submitted from form, if it exists, it's marked for deletion 
        if (oldDesc != null)
          nonExistingDescriptions.add(oldDesc);
      }
    }
    
    // Delete non existing descriptions
    for (CourseDescription desc: nonExistingDescriptions) {
      courseDescriptionDAO.delete(desc);
    }
    
    // Remove education types and subtypes

    List<CourseEducationType> courseEducationTypes = module.getCourseEducationTypes();
    for (int i = courseEducationTypes.size() - 1; i >= 0; i--) {
      CourseEducationType courseEducationType = courseEducationTypes.get(i);
      if (!chosenEducationTypes.containsKey(courseEducationType.getEducationType().getId())) {
        courseEducationTypeDAO.delete(courseEducationType);
      }
      else {
        Vector<Long> v = chosenEducationTypes.get(courseEducationType.getEducationType().getId());
        List<CourseEducationSubtype> courseEducationSubtypes = courseEducationType.getCourseEducationSubtypes();
        for (int j = courseEducationSubtypes.size() - 1; j >= 0; j--) {
          CourseEducationSubtype moduleEducationSubtype = courseEducationSubtypes.get(j);
          if (!v.contains(moduleEducationSubtype.getEducationSubtype().getId())) {
            courseEducationType.removeSubtype(moduleEducationSubtype);
          }
        }
      }
    }

    // Add education types and subtypes

    for (Long educationTypeId : chosenEducationTypes.keySet()) {
      EducationType educationType = educationTypeDAO.findById(educationTypeId);
      CourseEducationType courseEducationType;
      if (!module.contains(educationType)) {
        courseEducationType = courseEducationTypeDAO.create(module, educationType);
      }
      else {
        courseEducationType = module.getCourseEducationTypeByEducationTypeId(educationTypeId);
      }
      for (Long educationSubtypeId : chosenEducationTypes.get(educationTypeId)) {
        EducationSubtype educationSubtype = educationType.getEducationSubtypeById(educationSubtypeId);
        if (!courseEducationType.contains(educationSubtype)) {
          educationSubtypeDAO.create(courseEducationType, educationSubtype);
        }
      }
    }

    // Module components

    int rowCount = requestContext.getInteger("componentsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "componentsTable." + i;
      String componentName = requestContext.getString(colPrefix + ".name");
      Double componentLength = requestContext.getDouble(colPrefix + ".length");
      String componentDescription = requestContext.getString(colPrefix + ".description");
      Long componentId = requestContext.getLong(colPrefix + ".componentId");

      // TODO Component length; should be just hours but it currently depends on the default time unit - ok?  
      EducationalTimeUnit componentTimeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();

      if (componentId == -1) {
        componentId = moduleComponentDAO.create(module, componentLength, componentTimeUnit, componentName,
            componentDescription).getId();
      }
      else {
        moduleComponentDAO.update(moduleComponentDAO.findById(componentId), componentLength, componentTimeUnit,
            componentName, componentDescription);
      }
    }

    // Module basic information

    Long subjectId = requestContext.getLong("subject");
    Subject subject = subjectDAO.findById(subjectId);
    Integer courseNumber = requestContext.getInteger("courseNumber");
    String name = requestContext.getString("name");
    String description = requestContext.getString("description");
    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    Double moduleLength = requestContext.getDouble("moduleLength");
    Long moduleLengthTimeUnitId = requestContext.getLong("moduleLengthTimeUnit");
    Long maxParticipantCount = requestContext.getLong("maxParticipantCount");
    String tagsText = requestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<Tag>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    EducationalTimeUnit moduleLengthTimeUnit = educationalTimeUnitDAO.findById(moduleLengthTimeUnitId);

    
    moduleDAO.update(module, name, subject, courseNumber, moduleLength, moduleLengthTimeUnit, description, maxParticipantCount, loggedUser);

    // Tags

    moduleDAO.updateTags(module, tagEntities);
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
