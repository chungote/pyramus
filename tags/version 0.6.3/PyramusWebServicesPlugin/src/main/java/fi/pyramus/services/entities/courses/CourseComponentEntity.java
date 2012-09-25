package fi.pyramus.services.entities.courses;

public class CourseComponentEntity {
  
  public CourseComponentEntity() {
  }

  public CourseComponentEntity(Long id, String name, String description, Double length, Long lengthUnitId) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public Long getLengthUnitId() {
    return lengthUnitId;
  }

  public void setLengthUnitId(Long lengthUnitId) {
    this.lengthUnitId = lengthUnitId;
  }

  private Long id;
  private String name;
  private String description;
  private Double length;
  private Long lengthUnitId;
}
