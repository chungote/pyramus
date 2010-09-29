package fi.pyramus.domainmodel.help;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import fi.pyramus.domainmodel.users.User;

@Entity
@Inheritance (strategy=InheritanceType.JOINED)
@Indexed
public class HelpItem {

  public Long getId() {
    return id;
  }
  
  public HelpFolder getParent() {
    return parent;
  }
  
  public void setParent(HelpFolder parent) {
    this.parent = parent;
  }
  
  public Date getCreated() {
    return created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public User getCreator() {
    return creator;
  }
  
  public void setCreator(User creator) {
    this.creator = creator;
  }
  
  public Date getLastModified() {
    return lastModified;
  }
  
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }
  
  public User getLastModifier() {
    return lastModifier;
  }
  
  public void setLastModifier(User lastModifier) {
    this.lastModifier = lastModifier;
  }
  
  public Integer getIndexColumn() {
    return indexColumn;
  }
  
  public void setIndexColumn(Integer indexColumn) {
    this.indexColumn = indexColumn;
  }
  
  public List<HelpItemTitle> getTitles() {
    return titles;
  }
  
  public void setTitles(List<HelpItemTitle> titles) {
    this.titles = titles;
  }
  
  @Transient
  public void addTitle(HelpItemTitle helpItemTitle) {
    if (helpItemTitle.getItem() != null) {
      helpItemTitle.getItem().removeTitle(helpItemTitle);
    }
      
    helpItemTitle.setItem(this);
    titles.add(helpItemTitle);
  }
  
  @Transient
  public void removeTitle(HelpItemTitle helpItemTitle) {
    helpItemTitle.setItem(null);
    titles.remove(helpItemTitle);
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="HelpItem")  
  @TableGenerator(name="HelpItem", allocationSize=1)
  @DocumentId
  private Long id;
  
  @ManyToOne 
  @JoinColumn (name="parent")
  private HelpFolder parent;
  
  @Column (nullable=false)
  @NotNull
  private Integer indexColumn;
  
  @Column (nullable=false)
  @NotNull
  @Temporal (TemporalType.TIMESTAMP)
  @Field (index=Index.UN_TOKENIZED, store=Store.YES)
  @DateBridge (resolution=Resolution.MILLISECOND)
  private Date lastModified;
  
  @Column (nullable=false)
  @Temporal (TemporalType.TIMESTAMP)
  @org.hibernate.search.annotations.Field (index=Index.UN_TOKENIZED, store=Store.YES)
  @DateBridge (resolution=Resolution.MILLISECOND)
  private Date created;

  @ManyToOne
  @JoinColumn (name="creator")
  private User creator;
  
  @ManyToOne
  @JoinColumn (name="lastModifier")
  private User lastModifier;
  
  @OneToMany (cascade = CascadeType.ALL, mappedBy="item")
  private List<HelpItemTitle> titles = new ArrayList<HelpItemTitle>();
} 
