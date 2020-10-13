package info.wallyson.dto;

import info.wallyson.entity.Exercise;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDTO {
  private String id;
  @NotBlank private String name;
  private String description;
  private String imageUrl;
  private String createdBy;

  public Exercise toEntity() {
    return new Exercise(null, this.name, this.description, this.imageUrl, this.createdBy);
  }
}
