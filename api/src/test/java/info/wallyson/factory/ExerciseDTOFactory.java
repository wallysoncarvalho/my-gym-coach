package info.wallyson.factory;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.dto.ExerciseImageDTO;
import info.wallyson.entity.Exercise;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExerciseDTOFactory {

  public static List<Exercise> dtoToEntity(List<ExerciseDTO> exercises) {
    return exercises.stream().map(ExerciseDTO::toEntity).collect(Collectors.toList());
  }

  public static ExerciseDTO exercise() {
    return ExerciseDTO.builder()
        .id(UUID.randomUUID().toString())
        .name("Machine Row")
        .description(
            "To start this exercise sit on the bench with the chest forward and this is more data.")
        .imageUrl("/image/idk/exercise.png")
        .createdBy("8ca253ab-3018-4140-b90e-7afffde92327")
        .build();
  }

  public static List<ExerciseDTO> exerciseList() {
    return List.of(
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 1")
            .description("this exercise do something")
            .imageUrl("address of the image remotely or local..")
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 2")
            .description("this exercise do something")
            .imageUrl("address of the image remotely or local..")
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 3")
            .description("this exercise do something")
            .imageUrl("address of the image remotely or local..")
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 1")
            .description("this exercise do something")
            .imageUrl("address of the image remotely or local..")
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 2")
            .description("this exercise do something")
            .imageUrl("address of the image remotely or local..")
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 3")
            .description("this exercise do something")
            .imageUrl("address of the image remotely or local..")
            .build());
  }

  public static List<ExerciseImageDTO> createdImages() {
    var img1 = new ExerciseImageDTO("image 1", "");
    var img2 = new ExerciseImageDTO("image 2", "");
    var img3 = new ExerciseImageDTO("image 3", "");
    return Arrays.asList(img1, img2, img3);
  }
}
