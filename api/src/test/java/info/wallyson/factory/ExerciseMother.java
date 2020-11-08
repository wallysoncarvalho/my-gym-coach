package info.wallyson.factory;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.entity.Exercise;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExerciseMother {
  private static final Set<String> IMAGES =
      Set.of(
          "8ca253ab-3018-4140-b90e-7afffde92327",
          "8yp253ab-3018-4140-b90e-00000000000",
          "http://teste.com");

  private static final Set<String> MUSCLES = Set.of("muscle1", "muscle2");

  public static List<Exercise> dtoToEntity(List<ExerciseDTO> exercises) {
    return exercises.stream().map(ExerciseDTO::toEntity).collect(Collectors.toList());
  }

  public static ExerciseDTO exercise() {
    return ExerciseDTO.builder()
        .id(UUID.randomUUID().toString())
        .name("Machine Row")
        .description(
            "To start this exercise sit on the bench with the chest forward and this is more data.")
        .images(IMAGES)
        .muscles(MUSCLES)
        .createdBy("8ca253ab-3018-4140-b90e-7afffde92327")
        .build();
  }

  public static List<ExerciseDTO> exerciseList() {
    return List.of(
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 1")
            .description("this exercise do something")
            .images(IMAGES)
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 2")
            .description("this exercise do something")
            .images(IMAGES)
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 3")
            .description("this exercise do something")
            .images(IMAGES)
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 1")
            .description("this exercise do something")
            .images(IMAGES)
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 2")
            .description("this exercise do something")
            .images(IMAGES)
            .build(),
        ExerciseDTO.builder()
            .id(UUID.randomUUID().toString())
            .name("exercise 3")
            .description("this exercise do something")
            .images(IMAGES)
            .build());
  }
}
