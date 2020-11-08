package info.wallyson.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exercise_muscle")
@AllArgsConstructor
@Getter
public class ExerciseMuscle {
  @Id @GeneratedValue private Long id;

  private String muscleName;
}
