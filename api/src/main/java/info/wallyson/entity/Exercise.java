package info.wallyson.entity;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(
    name = "EXERCISE",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"name"},
          name = "exercise_name")
    })
public class Exercise extends BaseEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank private String name;
  private String description;
  private String imageUrl;
  private String createdBy;

  public Exercise() {}

  public Exercise(
      UUID id, @NotBlank String name, String description, String imageUrl, String createdBy) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
    this.createdBy = createdBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Exercise)) return false;
    var exercise = (Exercise) o;
    return Objects.equals(getId(), exercise.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
