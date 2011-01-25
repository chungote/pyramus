package fi.pyramus.services.entities.grading;

import java.util.Date;

public class TransferCreditEntity extends CreditEntity {

  public TransferCreditEntity(Long id, Long studentId, Date date, Long gradeId, String verbalAssessment, Long assessingUserId, boolean archived,
      String courseName, Integer courseNumber, Double length, Long lenghtUnitId, Long schoolId, Long subjectId, String optionality) {
    super(id, studentId, date, gradeId, verbalAssessment, assessingUserId, archived);
    this.courseName = courseName;
    this.length = length;
    this.lenghtUnitId = lenghtUnitId;
    this.schoolId = schoolId;
    this.subjectId = subjectId;
    this.optionality = optionality;
  }

  public String getCourseName() {
    return courseName;
  }
  
  public Integer getCourseNumber() {
    return courseNumber;
  }

  public Double getLength() {
    return length;
  }

  public Long getLenghtUnitId() {
    return lenghtUnitId;
  }

  public Long getSchoolId() {
    return schoolId;
  }

  public Long getSubjectId() {
    return subjectId;
  }
  
  public String getOptionality() {
    return optionality;
  }
  
  private String courseName;
  private Integer courseNumber;
  private Double length;
  private Long lenghtUnitId;
  private Long schoolId;
  private Long subjectId;
  private String optionality;
}