package fi.pyramus.domainmodel.grading;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalLength;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.students.Student;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class TransferCredit extends Credit {

  public TransferCredit() {
    super();
    setCreditType(CreditType.TransferCredit);
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }
  
  public Integer getCourseNumber() {
    return courseNumber;
  }
  
  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public EducationalLength getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(EducationalLength courseLength) {
    this.courseLength = courseLength;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }
  
  public CourseOptionality getOptionality() {
    return optionality;
  }
  
  public void setOptionality(CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Student getStudent() {
    return student;
  }
  
  public void setStudent(Student student) {
    this.student = student;
  }

  @ManyToOne  
  @JoinColumn(name="student")
  private Student student;
  
  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String courseName;

  private Integer courseNumber;
  
  @OneToOne
  @JoinColumn(name = "courseLength")
  private EducationalLength courseLength;
  
  @Column
  @Enumerated (EnumType.STRING)
  private CourseOptionality optionality;

  @ManyToOne
  @JoinColumn(name = "school")
  private School school;

  @ManyToOne
  @JoinColumn(name = "subject")
  private Subject subject;
}