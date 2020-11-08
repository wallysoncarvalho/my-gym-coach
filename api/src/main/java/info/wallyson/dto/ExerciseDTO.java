package info.wallyson.dto;

import info.wallyson.entity.Exercise;
import info.wallyson.entity.ExerciseImage;
import info.wallyson.entity.ExerciseMuscle;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class ExerciseDTO {
  private String id;
  @NotBlank private String name;
  private String description;
  private Set<String> images;
  private Set<String> muscles;
  @NotBlank private String createdBy;

  public Exercise toEntity() {
    return new Exercise(
        null, this.name, this.description, imagesToSet(), musclesToSet(), this.createdBy);
  }

  private Set<ExerciseImage> imagesToSet() {
    return this.images != null
        ? this.images.stream().map(img -> new ExerciseImage(null, img)).collect(Collectors.toSet())
        : null;
  }

  private Set<ExerciseMuscle> musclesToSet() {
    return this.muscles != null
        ? this.muscles.stream()
            .map(name -> new ExerciseMuscle(null, name))
            .collect(Collectors.toSet())
        : null;
  }

  public static ExerciseDTO fromEntity(Exercise ex) {
    var images = ex.getImages().stream().map(ExerciseImage::getId_url).collect(Collectors.toSet());
    var muscles =
        ex.getMuscles().stream().map(ExerciseMuscle::getMuscleName).collect(Collectors.toSet());
    var id = ex.getId() != null ? ex.getId().toString() : "";

    return ExerciseDTO.builder()
        .id(id)
        .name(ex.getName())
        .description(ex.getDescription())
        .images(images)
        .muscles(muscles)
        .createdBy(ex.getCreatedBy())
        .build();
  }
}
