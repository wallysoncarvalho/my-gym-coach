package info.wallyson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.wallyson.dto.ExerciseDTO;
import info.wallyson.dto.ExerciseImageDTO;
import info.wallyson.entity.Exercise;
import info.wallyson.factory.ExerciseMother;
import info.wallyson.service.ExerciseService;
import info.wallyson.utils.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ExerciseController.class)
class ExerciseControllerTest {
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired private MockMvc mockMvc;
  @MockBean private ExerciseService exerciseService;

  @Test
  @DisplayName("Should get exercise page 0 and max size 10")
  void should_get_exercise_page_with_default_params() throws Exception {
    var pageable = PageRequest.of(0, 10);
    var exerciseEntityList = ExerciseMother.dtoToEntity(ExerciseMother.exerciseList());
    var page =
        new PageImpl<>(exerciseEntityList, pageable, ExerciseMother.exerciseList().size());
    when(exerciseService.getExercises(any(Pageable.class))).thenReturn(page);
    var result =
        this.mockMvc
            .perform(get("/api/v1/exercises").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.pageable.pageSize").value(10))
            .andExpect(jsonPath("$.pageable.pageNumber").value(0))
            .andReturn();
    verify(exerciseService, times(1)).getExercises(any(Pageable.class));
    assertEquals(200, result.getResponse().getStatus());
  }

  @Test
  @DisplayName("Should use default for invalid page and size params")
  void should_return_bad_request_with_invalid_query_params() throws Exception {
    var pageable = PageRequest.of(0, 10);
    var exerciseEntityList = ExerciseMother.dtoToEntity(ExerciseMother.exerciseList());
    var page =
        new PageImpl<>(exerciseEntityList, pageable, ExerciseMother.exerciseList().size());
    when(exerciseService.getExercises(any(Pageable.class))).thenReturn(page);
    var result =
        this.mockMvc
            .perform(
                get("/api/v1/exercises")
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("page", "asdasd")
                    .queryParam("size", "{asdasd:asdsd}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageSize").value(10))
            .andExpect(jsonPath("$.pageable.pageNumber").value(0))
            .andReturn();
    verify(exerciseService, times(1)).getExercises(any(Pageable.class));
    assertEquals(200, result.getResponse().getStatus());
  }

  @Test
  @DisplayName("Should create a new exercise with all information")
  void should_create_new_exercise_and_return() throws Exception {
    var exercise = ExerciseMother.exercise();

    when(exerciseService.createExercise(any(Exercise.class))).thenReturn(exercise.toEntity());

    this.mockMvc
        .perform(
            post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(JsonUtils.toJson(exercise))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(exercise.getName()))
        .andExpect(jsonPath("$.createdBy").value(exercise.getCreatedBy()))
        .andExpect(jsonPath("$.images").isArray())
        .andExpect(jsonPath("$.muscles").isArray())
        .andReturn();

    verify(exerciseService, times(1)).createExercise(any(Exercise.class));
  }

  @Test
  @DisplayName("Should get an exercise by its id")
  void should_get_exercise_by_id() throws Exception {
    var exercise = ExerciseMother.exercise();
    when(exerciseService.getExercise(exercise.getId()))
        .thenReturn(Optional.of(exercise.toEntity()));

    this.mockMvc
        .perform(
            get("/api/v1/exercises/" + exercise.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(exercise.getName()))
        .andExpect(jsonPath("$.createdBy").value(exercise.getCreatedBy()))
        .andExpect(jsonPath("$.images").isArray())
        .andExpect(jsonPath("$.muscles").isArray())
        .andReturn();

    verify(exerciseService, times(1)).getExercise(exercise.getId());
  }

  @Test
  @DisplayName("Should return not found when theres no exercise with provided id")
  void should_not_find_exercise() throws Exception {
    var id = "unknown id";
    when(exerciseService.getExercise(id)).thenReturn(Optional.empty());

    this.mockMvc
        .perform(get("/api/v1/exercises/" + id).accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").exists())
        .andReturn();

    verify(exerciseService, times(1)).getExercise(id);
  }

  @Test
  @DisplayName("Should delete an exercise by its id")
  void should_delete_exercise() throws Exception {
    var exercise = ExerciseMother.exercise();
    when(exerciseService.deleteExercise(exercise.getId()))
        .thenReturn(Optional.of(exercise.toEntity()));

    this.mockMvc
        .perform(
            delete("/api/v1/exercises/" + exercise.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(exercise.getName()))
        .andExpect(jsonPath("$.createdBy").value(exercise.getCreatedBy()))
        .andExpect(jsonPath("$.images").isArray())
        .andExpect(jsonPath("$.muscles").isArray())
        .andReturn();

    verify(exerciseService, times(1)).deleteExercise(exercise.getId());
  }

  @Test
  @DisplayName("Should fail to delete by id a nonexistent exercise")
  void should_fail_to_delete_nonexistent_exercise() throws Exception {
    var id = UUID.randomUUID().toString();
    when(exerciseService.deleteExercise(id)).thenReturn(Optional.empty());

    this.mockMvc
        .perform(delete("/api/v1/exercises/" + id).accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").exists())
        .andReturn();

    verify(exerciseService, times(1)).deleteExercise(id);
  }

  @Test
  @DisplayName("Should fail to create an exercise without a name and id of creator")
  void should_return_validation_error() throws Exception {
    this.mockMvc
        .perform(
            post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtils.toJson(ExerciseDTO.builder().build()))
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.name").exists())
        .andExpect(jsonPath("$.createdBy").exists())
        .andReturn();
  }

  @Test
  @DisplayName(
      "Should upload JPEG images to local folder. Returns uploaded images and download link.")
  void should_upload_images_to_local_folder() throws Exception {
    var image1 = new MockMultipartFile("images", "image-name.jpg", "image/jpeg", new byte[1024]);

    when(exerciseService.storeMultipartFiles(List.of(image1)))
        .thenReturn(List.of("91a757a5-e05d-4014-ae92-bfe07c871aaa"));

    var result =
        this.mockMvc
            .perform(multipart("/api/v1/exercises/images").file(image1))
            .andExpect(status().isOk())
            .andReturn();

    verify(exerciseService, times(1)).storeMultipartFiles(List.of(image1));
    var strResponse = result.getResponse().getContentAsString();
    var imagesList = mapper.readValue(strResponse, ExerciseImageDTO[].class);

    assertEquals(1, imagesList.length);
    assertFalse(imagesList[0].getName().isBlank());
    assertFalse(imagesList[0].getUrl().isBlank());
  }

  @Test
  @DisplayName("Should fail to upload an image with empty content")
  void should_fail_to_upload_due_to_empty_content() throws Exception {
    var image1 = new MockMultipartFile("images", "image-name.jpg", "image/jpeg", "".getBytes());

    this.mockMvc
        .perform(multipart("/api/v1/exercises/images").file(image1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.errors").isArray())
        .andReturn();
  }

  @Test
  @DisplayName("Should fail to upload an image with content different from JPEG, JPG or PNG")
  void should_fail_to_upload_due_to_invalid_content_type() throws Exception {
    var image1 = new MockMultipartFile("images", "file.txt", "text/plain", "".getBytes());

    this.mockMvc
        .perform(multipart("/api/v1/exercises/images").file(image1))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.errors").isArray())
        .andReturn();
  }

  @Test
  @DisplayName("Should fail to upload file with more than 1MB")
  void should_fail_to_upload_files_wit_more_than_1MB() throws Exception {
    byte[] bytes = new byte[1024 * 1024 * 10];
    var image = new MockMultipartFile("images", "image.jpg", "image/jpeg", bytes);

    this.mockMvc
        .perform(multipart("/api/v1/exercises/images").file(image))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.errors").isArray())
        .andReturn();
  }

  @Test
  @DisplayName("Should download an image given the image id stored on local storage")
  void should_download_image_given_id(@TempDir Path tempDir) throws Exception {
    var fileName = "c0ffddb7-2fe0-4494-a57d-a8f068332668";
    var file = File.createTempFile(tempDir.toString() + "/" + fileName, "");

    Files.writeString(file.toPath(), "this is the file content");

    when(exerciseService.getFileFromImageDir(file.getName()))
        .thenReturn(new FileSystemResource(file));

    var response =
        this.mockMvc
            .perform(get("/api/v1/exercises/images/" + file.getName()))
            .andExpect(status().isOk())
            .andReturn();

    var fileSize = response.getResponse().getContentLength();
    var contentDisposition = response.getResponse().getHeader("Content-Disposition");

    assertTrue(fileSize > 0);
    assertNotNull(contentDisposition);
    assertTrue(contentDisposition.contains(fileName));
  }

  @Test
  @DisplayName("Should return 404 when image doesn't exist")
  void should_return_404_when_image_doesnt_exist() throws Exception {
    var fileName = "invalid-file-name";
    when(exerciseService.getFileFromImageDir(fileName)).thenReturn(new FileSystemResource(""));
    this.mockMvc
        .perform(get("/api/v1/exercises/images/" + fileName))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists())
        .andReturn();
  }
}
