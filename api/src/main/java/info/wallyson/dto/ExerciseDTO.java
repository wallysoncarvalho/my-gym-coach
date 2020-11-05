package info.wallyson.dto;

import info.wallyson.entity.Exercise;
import info.wallyson.entity.ExerciseImage;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ExerciseDTO {
  private String id;
  @NotBlank private String name;
  private String description;
  private List<String> images;
  @NotBlank private String createdBy;

  public Exercise toEntity() {
    var imagesSet =
        this.images != null
            ? this.images.stream()
                .map(img -> new ExerciseImage(null, img))
                .collect(Collectors.toSet())
            : null;

    return new Exercise(null, this.name, this.description, imagesSet, this.createdBy);
  }

  public static ExerciseDTO fromEntity(Exercise ex) {
    var images = ex.getImages().stream().map(ExerciseImage::getId_url).collect(Collectors.toList());
    var id = ex.getId() != null ? ex.getId().toString() : "";

    return ExerciseDTO.builder()
        .id(id)
        .name(ex.getName())
        .description(ex.getDescription())
        .images(images)
        .createdBy(ex.getCreatedBy())
        .build();
  }
}
