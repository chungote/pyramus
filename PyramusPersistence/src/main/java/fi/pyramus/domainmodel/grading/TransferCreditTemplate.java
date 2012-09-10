package fi.pyramus.domainmodel.grading;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class TransferCreditTemplate {
  
  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }
  
  public List<TransferCreditTemplateCourse> getCourses() {
    return courses;
  }
  
  public void setCourses(List<TransferCreditTemplateCourse> courses) {
    this.courses = courses;
  }
  
  public void addCourse(TransferCreditTemplateCourse course) {
    if (!courses.contains(course)) {
      if (course.getTransferCreditTemplate() != null) {
        course.getTransferCreditTemplate().removeCourse(course);
      }
      
      course.setTransferCreditTemplate(this);
      courses.add(course);
    } else {
      throw new PersistenceException("Template already contains this course");
    }
  }
  
  public void removeCourse(TransferCreditTemplateCourse course) {
    if (courses.contains(course)) {
      course.setTransferCreditTemplate(null);
      courses.remove(course);
    } else {
      throw new PersistenceException("Template does not contain this course");
    }
  } 

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="TransferCreditTemplate")  
  @TableGenerator(name="TransferCreditTemplate", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotEmpty
  @Column(nullable = false)
  private String name;
  
  @Version
  @Column(nullable = false)
  private Long version;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @IndexColumn (name = "indexColumn")
  @JoinColumn (name="transferCreditTemplate")
  private List<TransferCreditTemplateCourse> courses = new ArrayList<TransferCreditTemplateCourse>();
}
