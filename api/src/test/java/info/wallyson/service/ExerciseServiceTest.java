package info.wallyson.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.entity.Exercise;
import info.wallyson.exception.ApiException;
import info.wallyson.factory.ExerciseMother;
import info.wallyson.repository.ExerciseRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        ExerciseMother.dtoToEntity(ExerciseMother.exerciseList().subList(0, 2));
    var page = new PageImpl<>(listWithTwoExercises, pageable, listWithTwoExercises.size());
    when(this.exerciseRepository.findAll(pageable)).thenReturn(page);
    var exercises = this.exerciseService.getExercises(pageable);
    verify(this.exerciseRepository, times(1)).findAll(pageable);
    assertEquals(listWithTwoExercises.size(), exercises.toList().size());
  }

  @Test
  @DisplayName("Should create a new exercise with only name and return created exercise with id")
  void should_create_new_exercise_on_database() {
    var exercise = ExerciseMother.exercise();

    var entity = exercise.toEntity();
    entity.setId(null);

    when(this.exerciseRepository.save(any(Exercise.class))).thenReturn(entity);
    var createdExercise = this.exerciseService.createExercise(exercise.toEntity());

    verify(this.exerciseRepository, times(1)).save(entity);
    assertEquals(exercise.getName(), createdExercise.getName());
  }

  @Test
  @DisplayName("Should throw error when trying to create an exercise with a blank name")
  void should_throw_error_when_creating_exercise_with_blank_name() {
    var exercise = ExerciseDTO.builder().build();
    when(this.exerciseRepository.save(new Exercise()))
        .thenThrow(ConstraintViolationException.class);
    assertThrows(
        ConstraintViolationException.class,
        () -> this.exerciseService.createExercise(exercise.toEntity()));
    verify(this.exerciseRepository, times(1)).save(new Exercise());
  }

  @Test
  @DisplayName("Should fail to create exercise with existing name")
  void should_throw_error_when_creating_exercise_with_existing_name() {
    var exerciseDTO = ExerciseDTO.builder().name("exercise").build();
    when(this.exerciseRepository.existsByName("exercise")).thenReturn(true);
    assertThrows(
        ApiException.class, () -> this.exerciseService.createExercise(exerciseDTO.toEntity()));
    verify(this.exerciseRepository, times(1)).existsByName("exercise");
  }

  @Test
  @DisplayName("Should get an exercise by its id")
  void should_get_exercise_by_id() {
    var exercise = ExerciseMother.exercise().toEntity();
    exercise.setId(UUID.randomUUID());
    var id = exercise.getId().toString();
    when(this.exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
    var foundExercise = this.exerciseService.getExercise(id);
    assertTrue(foundExercise.isPresent());
    assertEquals(exercise, foundExercise.get());
  }

  @Test
  @DisplayName("Should return empty optional when an exercise don't exists")
  void should_return_empty_optional_for_nonexistent_exercise_() {
    var id = UUID.randomUUID().toString();
    when(this.exerciseRepository.findById(id)).thenReturn(Optional.empty());
    var foundExercise = this.exerciseService.getExercise(id);
    assertFalse(foundExercise.isPresent());
  }

  @Test
  @DisplayName("Should delete exercise by its id and return it")
  void should_delete_exercise_by_id_and_return_it() {
    var exercise = ExerciseMother.exercise().toEntity();
    exercise.setId(UUID.randomUUID());
    var id = exercise.getId().toString();
    when(this.exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
    var foundExercise = this.exerciseService.deleteExercise(id);
    assertTrue(foundExercise.isPresent());
    assertEquals(exercise, foundExercise.get());
  }

  @Test
  @DisplayName("Should return empty optional when deleting nonexistent exercise")
  void should_return_empty_optional_when_deleting_nonexistent_exercise() {
    var id = UUID.randomUUID().toString();
    when(this.exerciseRepository.findById(id)).thenReturn(Optional.empty());
    var foundExercise = this.exerciseService.deleteExercise(id);
    assertFalse(foundExercise.isPresent());
  }

  @Test
  @DisplayName("Should create an exercise with images")
  void should_create_exercise_with_images() {
    var exercise = ExerciseMother.exercise();

    when(this.exerciseRepository.save(any(Exercise.class)))
        .thenReturn(ExerciseMother.exercise().toEntity());

    var createdExercise = this.exerciseService.createExercise(exercise.toEntity());

    verify(this.exerciseRepository, times(1)).save(ExerciseMother.exercise().toEntity());

    assertEquals(exercise.getName(), createdExercise.getName());
  }

  @Test
  @DisplayName("Should return an empty string array when there's no image provided")
  void should_return_empty_list_when_no_image_provided(@TempDir Path tempDir) {
    var service = new ExerciseService(null, tempDir.toString());
    List<MultipartFile> images = List.of();
    var createdImagesNames = service.storeMultipartFiles(images);
    assertEquals(0, createdImagesNames.size());
  }

  @Test
  @DisplayName("Should store images on local storage")
  void should_store_images_on_local_storage(@TempDir Path tempDir) {
    var service = new ExerciseService(null, tempDir.toString());

    List<MultipartFile> images =
        List.of(
            new MockMultipartFile("image-name.jpg", "content_of_image".getBytes()),
            new MockMultipartFile("image-name.jpg", "content_of_image".getBytes()));

    var createdImagesNames = service.storeMultipartFiles(images);

    assertEquals(2, createdImagesNames.size());

    createdImagesNames.forEach(name -> assertTrue(Files.exists(tempDir.resolve(name))));
  }

  @Test
  @DisplayName("Should ignore empty multipart content")
  void should_ignore_empty_multipart_content(@TempDir Path tempDir) {
    var service = new ExerciseService(null, tempDir.toString());

    List<MultipartFile> images =
        List.of(
            new MockMultipartFile("image-name.jpg", "images content".getBytes()),
            new MockMultipartFile("image-name.jpg", "images content".getBytes()),
            new MockMultipartFile("image-name.jpg", "".getBytes()));

    var createdImagesNames = service.storeMultipartFiles(images);

    assertEquals(2, createdImagesNames.size());

    createdImagesNames.forEach(name -> assertTrue(Files.exists(tempDir.resolve(name))));
  }

  @Test
  @DisplayName("Should throw api exception while trying to save file on invalid path")
  void should_throw_exception_while_storing_image_on_invalid_path() {
    var service = new ExerciseService(null, "//a/dad//asd/invalid/path");
    List<MultipartFile> image =
        List.of(new MockMultipartFile("image-name.jpg", "images content".getBytes()));
    assertThrows(ApiException.class, () -> service.storeMultipartFiles(image));
  }

  @Test
  @DisplayName("Get file from image dir given it's name")
  void get_file_from_image_dir(@TempDir Path tempDir) throws IOException {
    var file = new File(tempDir.toString() + "/arquivo.txt");

    assertTrue(file.createNewFile());

    var service = new ExerciseService(null, tempDir.toString());
    var fileResource = service.getFileFromImageDir(file.getName());

    assertEquals(file.getName(), fileResource.getFilename());
    assertTrue(file.exists());
    assertTrue(fileResource.exists());
  }

  @Test
  @DisplayName("Should return invalid resource when file not found")
  void should_return_invalid_resource_when_file_not_found(@TempDir Path tempDir) {
    var service = new ExerciseService(null, tempDir.toString());
    var fileResource = service.getFileFromImageDir(UUID.randomUUID().toString());

    assertFalse(fileResource.exists());
  }
}
