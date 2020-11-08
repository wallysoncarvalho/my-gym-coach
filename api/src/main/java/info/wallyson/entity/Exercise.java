package info.wallyson.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "exercise_id")
  private Set<ExerciseImage> images;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "exercise_id")
  private Set<ExerciseMuscle> muscles;

  @NotBlank private String createdBy;

  public Exercise() {}

  public Exercise(
      UUID id,
      @NotBlank String name,
      String description,
      Set<ExerciseImage> images,
      Set<ExerciseMuscle> muscles,
      String createdBy) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.images = images;
    this.muscles = muscles;
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
