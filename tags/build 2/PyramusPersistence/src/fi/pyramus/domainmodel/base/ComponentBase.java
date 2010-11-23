package fi.pyramus.domainmodel.base;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class ComponentBase {

  public Long getId() {
    return id;
  }

  @SuppressWarnings("unused")
  private void setId(Long id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public EducationalLength getLength() {
    return length;
  }
  
  public void setLength(EducationalLength length) {
    this.length = length;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  @Id 
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ComponentBase")  
  @TableGenerator(name="ComponentBase", allocationSize=1)
  private Long id;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field (index=Index.TOKENIZED)
  private String name;

  @Basic (fetch = FetchType.LAZY)
  @Column (length=1073741824)
  @Field (index=Index.TOKENIZED)
  private String description;
  
  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "length")
  private EducationalLength length = new EducationalLength();

  @NotNull
  @Column(nullable = false)
  @Field (index=Index.UN_TOKENIZED)
  private Boolean archived = Boolean.FALSE;

}