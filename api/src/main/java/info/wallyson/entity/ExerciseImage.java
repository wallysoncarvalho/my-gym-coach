package info.wallyson.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exercise_images")
@AllArgsConstructor
@Getter
public class ExerciseImage {
  @Id @GeneratedValue private Long id;

  private String id_url;
}
