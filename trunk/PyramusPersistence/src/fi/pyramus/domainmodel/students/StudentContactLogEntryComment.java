package fi.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Index;

import fi.pyramus.domainmodel.base.ArchivableEntity;
import fi.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

/**
 * StudentContactLogEntryComment class defines a message bind to a contact log entry
 * when contact has been made between him/her and f.ex. a teacher.
 * 
 * Properties of StudentContactLogEntryComment
 * - internal id for internal linking
 * - entry that the message is linked to
 * - text for textual message or description of the entry
 * - entry date to identify the date/time of message
 * - creatorName to tell who the student was in contact with
 * 
 * @author antti.viljakainen
 */

@Entity
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedContactLogEntryComment",
     impl=ArchivedEntityFilterFactory.class
  )
)
public class StudentContactLogEntryComment implements ArchivableEntity {

  /**
   * Returns internal unique id
   * 
   * @return unique id
   */
  public Long getId() {
    return id;
  }
  
  /**
   * Sets textual message or description associated with this entry
   * 
   * @param text Textual message or description
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Returns textual message or description associated with this entry.
   * 
   * @return Textual mesasge or description
   */
  public String getText() {
    return text;
  }

  /**
   * Sets date for this comment.
   * 
   * @param entryDate New date
   */
  public void setCommentDate(Date commentDate) {
    this.commentDate = commentDate;
  }

  /**
   * Returns date of this comment.
   * 
   * @return Entry date
   */
  public Date getCommentDate() {
    return commentDate;
  }

  /**
   * Sets creator for this entry.
   * 
   * @param creator New creator
   */
  public void setCreatorName(String creator) {
    this.creatorName = creator;
  }

  /**
   * Returns the creator of this entry.
   * 
   * @return The creator
   */
  public String getCreatorName() {
    return creatorName;
  }

  @Override
  public Boolean getArchived() {
    return archived;
  }

  @Override
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public void setEntry(StudentContactLogEntry entry) {
    this.entry = entry;
  }

  public StudentContactLogEntry getEntry() {
    return entry;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentContactLogEntryComment")  
  @TableGenerator(name="StudentContactLogEntryComment", allocationSize=1)
  private Long id;

  @ManyToOne (optional = false)
  @JoinColumn (name = "entry")
  private StudentContactLogEntry entry;
  
  @Column (length=1073741824)
  private String text;
  
  private String creatorName;

  @Temporal (value=TemporalType.TIMESTAMP)
  private Date commentDate;
  
  @NotNull
  @Column (nullable = false)
  @Field (index = Index.TOKENIZED)
  private Boolean archived = Boolean.FALSE;

  @Version
  @NotNull
  @Column(nullable = false)
  private Long version;
}
