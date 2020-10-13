package info.wallyson.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.entity.Exercise;
import info.wallyson.exception.ApiException;
import info.wallyson.factory.ExerciseDTOFactory;
import info.wallyson.repository.ExerciseRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Exercise service tests \uD83D\uDE30!")
@ExtendWith(SpringExtension.class)
class ExerciseServiceTest {
  @InjectMocks private ExerciseService exerciseService;

  @Mock private ExerciseRepository exerciseRepository;

  @Test
  @DisplayName("Should get a page with 2 exercises.")
  void should_get_page_with_two_exercises() {
    var pageable = PageRequest.of(0, 2);
    var listWithTwoExercises =
        ExerciseDTOFactory.dtoToEntity(ExerciseDTOFactory.exerciseList().subList(0, 2));
    var page = new PageImpl<>(listWithTwoExercises, pageable, listWithTwoExercises.size());
    when(this.exerciseRepository.findAll(pageable)).thenReturn(page);
    var exercises = this.exerciseService.getExercises(pageable);
    verify(this.exerciseRepository, times(1)).findAll(pageable);
    assertEquals(listWithTwoExercises.size(), exercises.toList().size());
  }

  @Test
  @DisplayName("Should create a new exercise with only name and return created exercise with id")
  void should_create_new_exercise_on_database() {
    var exercise = ExerciseDTOFactory.exercise();
    when(this.exerciseRepository.save(any(Exercise.class)))
        .thenReturn(ExerciseDTOFactory.exercise().toEntity());
    var createdExercise = this.exerciseService.createExercise(exercise);
    verify(this.exerciseRepository, times(1)).save(ExerciseDTOFactory.exercise().toEntity());
    assertEquals(exercise.getName(), createdExercise.getName());
  }

  @Test
  @DisplayName("Should throw error when trying to create an exercise with a blank name")
  void should_throw_error_when_creating_exercise_with_blank_name() {
    var exerciseDTO = ExerciseDTO.builder().build();
    when(this.exerciseRepository.save(new Exercise()))
        .thenThrow(ConstraintViolationException.class);
    assertThrows(
        ConstraintViolationException.class, () -> this.exerciseService.createExercise(exerciseDTO));
    verify(this.exerciseRepository, times(1)).save(new Exercise());
  }

  @Test
  @DisplayName(
      "Should throw error when trying to create an exercise that has the same name that an existing exercise")
  void should_throw_error_when_creating_exercise_with_existing_name() {
    var exerciseDTO = ExerciseDTO.builder().name("exercise").build();
    when(this.exerciseRepository.existsByName("exercise")).thenReturn(true);
    assertThrows(ApiException.class, () -> this.exerciseService.createExercise(exerciseDTO));
    verify(this.exerciseRepository, times(1)).existsByName("exercise");
  }

  @Test
  @DisplayName("Should store images on local storage")
  void should_store_images_on_local_storage(@TempDir Path tempDir) {
    var service = new ExerciseService(null, tempDir.toString());

    List<MultipartFile> images =
        List.of(
            new MockMultipartFile("image-name.jpg", "".getBytes()),
            new MockMultipartFile("image-name.jpg", "".getBytes()),
            new MockMultipartFile("image-name.jpg", "".getBytes()));

    var createdImagesNames = service.storeImages(images);

    assertEquals(3, createdImagesNames.size());

    createdImagesNames.forEach(
        name -> {
          assertTrue(Files.exists(tempDir.resolve(name)));
        });
  }
}
