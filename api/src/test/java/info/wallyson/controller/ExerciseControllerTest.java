package info.wallyson.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.wallyson.dto.ExerciseDTO;
import info.wallyson.dto.ExerciseImageDTO;
import info.wallyson.factory.ExerciseDTOFactory;
import info.wallyson.service.ExerciseService;
import info.wallyson.utils.JsonUtils;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ExerciseController.class)
class ExerciseControllerTest {
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired private MockMvc mockMvc;
  @MockBean private ExerciseService exerciseService;

  @Test
  @DisplayName("Make a GET request to exercises endpoint and expect HTTP status 200")
  void should_get_status_isOk() throws Exception {
    var result =
        this.mockMvc
            .perform(get("/api/v1/exercises").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  @Test
  @DisplayName("Should make request without paging params and return page = 0 of size = 10")
  void should_get_page_with_default_params() throws Exception {
    var pageable = PageRequest.of(0, 10);
    var exerciseEntityList = ExerciseDTOFactory.dtoToEntity(ExerciseDTOFactory.exerciseList());
    var page =
        new PageImpl<>(exerciseEntityList, pageable, ExerciseDTOFactory.exerciseList().size());
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
  @DisplayName(
      "Should fall to default params when invalid values for 'page' and 'size' are provided")
  void should_return_bad_request_with_invalid_query_params() throws Exception {
    var pageable = PageRequest.of(0, 10);
    var exerciseEntityList = ExerciseDTOFactory.dtoToEntity(ExerciseDTOFactory.exerciseList());
    var page =
        new PageImpl<>(exerciseEntityList, pageable, ExerciseDTOFactory.exerciseList().size());
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
  @DisplayName(
      "Make POST request to create a new exercise. Returns created exercise with ID and HTTP status 201")
  void should_create_new_exercise_and_return() throws Exception {
    when(exerciseService.createExercise(any(ExerciseDTO.class)))
        .thenReturn(ExerciseDTOFactory.exercise().toEntity());
    var result =
        this.mockMvc
            .perform(
                post("/api/v1/exercises")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("utf-8")
                    .content(JsonUtils.toJson(ExerciseDTOFactory.exercise()))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(ExerciseDTOFactory.exercise().getName()))
            .andExpect(jsonPath("$.createdBy").exists())
            .andReturn();

    verify(exerciseService, times(1)).createExercise(any(ExerciseDTO.class));
    assertNotNull(result.getResponse());
  }

  @Test
  @DisplayName(
      "Returns validation error when trying to create a new exercise with blank name and get HTTP status 400")
  void should_return_validation_error() throws Exception {
    var result =
        this.mockMvc
            .perform(
                post("/api/v1/exercises")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(JsonUtils.toJson(ExerciseDTO.builder().build()))
                    .characterEncoding("utf-8")
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().is(400))
            .andReturn();
    assertEquals("{\"name\":\"must not be blank\"}", result.getResponse().getContentAsString());
  }

  @Test
  @DisplayName(
      "Should upload JPEG images to local folder. Returns uploaded images and download link.")
  void should_upload_images_to_local_folder() throws Exception {
    when(exerciseService.storeImages(any()))
        .thenReturn(
            List.of(
                "91a757a5-e05d-4014-ae92-bfe07c871aaa",
                "5bbf4f87-bddc-4b6d-a673-06e61f5ba11b",
                "c553df8f-9f5d-4861-95ca-fd7c0a82f9ea"));

    var image1 = new MockMultipartFile("images", "image-name.jpg", "image/jpeg", "".getBytes());

    var result =
        this.mockMvc
            .perform(
                multipart("/api/v1/exercises/images")
                    .file(image1)
                    .contentType(MediaType.IMAGE_JPEG))
            .andExpect(status().isOk())
            .andReturn();

    verify(exerciseService, times(1)).storeImages(any());
    var strResponse = result.getResponse().getContentAsString();
    var imagesList = mapper.readValue(strResponse, ExerciseImageDTO[].class);

    assertEquals(3, imagesList.length);
    assertFalse(imagesList[0].getName().isBlank());
    assertFalse(imagesList[0].getUrl().isBlank());
  }
}
