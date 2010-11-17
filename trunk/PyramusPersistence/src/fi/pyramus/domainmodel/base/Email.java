package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Indexed
public class Email {
  
  public Long getId() {
    return id;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setDefaultAddress(Boolean defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public Boolean getDefaultAddress() {
    return defaultAddress;
  }

  public void setContactType(ContactType contactType) {
    this.contactType = contactType;
  }

  public ContactType getContactType() {
    return contactType;
  }

  public void setContactInfo(ContactInfo contactInfo) {
    this.contactInfo = contactInfo;
  }

  public ContactInfo getContactInfo() {
    return contactInfo;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Email")  
  @TableGenerator(name="Email", allocationSize=1)
  @DocumentId
  private Long id;
  
  @ManyToOne
  @JoinColumn (name = "contactType")
  private ContactType contactType;

  @NotNull
  @Column(nullable = false)
  @Field (index=Index.TOKENIZED)
  private Boolean defaultAddress = Boolean.FALSE;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field (index = Index.TOKENIZED, store = Store.NO) 
  private String address;

  @ManyToOne
  @JoinColumn(name="contactInfo")
  private ContactInfo contactInfo;

  @Version
  @Column(nullable = false)
  private Long version;
}
