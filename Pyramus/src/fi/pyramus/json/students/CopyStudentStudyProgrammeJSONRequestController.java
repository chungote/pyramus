package fi.pyramus.json.students;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.JSONRequestController;
import fi.pyramus.UserRole;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactInfoDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;

public class CopyStudentStudyProgrammeJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();

    Long studentId = NumberUtils.createLong(requestContext.getRequest().getParameter("studentId"));
    Student oldStudent = studentDAO.findById(studentId);

    AbstractStudent abstractStudent = oldStudent.getAbstractStudent();
    String firstName = oldStudent.getFirstName();
    String lastName = oldStudent.getLastName();
    String nickname = oldStudent.getNickname();
    String additionalInfo = oldStudent.getAdditionalInfo();
    Double previousStudies = null; // student.getPreviousStudies();
    Date studyTimeEnd = null; // student.getStudyTimeEnd();
    Date studyStartTime = null; // student.getStudyStartDate();
    Date studyEndTime = null; // student.getStudyEndDate();
    String studyEndText = null; // student.getStudyEndText();
    Language language = oldStudent.getLanguage();
    Municipality municipality = oldStudent.getMunicipality();
    StudentActivityType activityType = oldStudent.getActivityType();
    StudentExaminationType examinationType = oldStudent.getExaminationType();
    StudentEducationalLevel educationalLevel = oldStudent.getEducationalLevel();
    String education = oldStudent.getEducation();
    Nationality nationality = oldStudent.getNationality();
    School school = oldStudent.getSchool();
    StudyProgramme studyProgramme = null; // student.getStudyProgramme();
    StudentStudyEndReason studyEndReason = null; // student.getStudyEndReason();
    Boolean lodging = false; // oldStudent.getLodging();

    Student newStudent = studentDAO.create(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd,
        activityType, examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies,
        studyStartTime, studyEndTime, studyEndReason, studyEndText, lodging);
    
    // Contact info
    
    contactInfoDAO.update(newStudent.getContactInfo(), oldStudent.getContactInfo().getAdditionalInfo());

    // Addresses

    List<Address> addresses = oldStudent.getContactInfo().getAddresses();
    for (int i = 0; i < addresses.size(); i++) {
      Address add = addresses.get(i);
      addressDAO.create(newStudent.getContactInfo(), add.getContactType(), add.getName(), add.getStreetAddress(), add.getPostalCode(), add.getCity(),
          add.getCountry(), add.getDefaultAddress());
    }

    // Email addresses

    List<Email> emails = oldStudent.getContactInfo().getEmails();
    for (int i = 0; i < emails.size(); i++) {
      Email email = emails.get(i);
      emailDAO.create(newStudent.getContactInfo(), email.getContactType(), email.getDefaultAddress(), email.getAddress());
    }
    
    // Phone numbers

    List<PhoneNumber> phoneNumbers = oldStudent.getContactInfo().getPhoneNumbers();
    for (int i = 0; i < phoneNumbers.size(); i++) {
      PhoneNumber phoneNumber = phoneNumbers.get(i);
      phoneNumberDAO.create(newStudent.getContactInfo(), phoneNumber.getContactType(), phoneNumber.getDefaultNumber(), phoneNumber.getNumber());
    }

    String redirectURL = requestContext.getRequest().getContextPath() + "/students/editstudent.page?student=" + newStudent.getAbstractStudent().getId();
    String refererAnchor = requestContext.getRefererAnchor();

    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}