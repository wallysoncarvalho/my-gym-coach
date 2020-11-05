package info.wallyson.controller;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.dto.ExerciseImageDTO;
import info.wallyson.entity.Exercise;
import info.wallyson.exception.ApiException;
import info.wallyson.service.ExerciseService;
import info.wallyson.validations.exerciseimage.ValidExerciseImage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/v1/exercises")
public class ExerciseController {
  private final ExerciseService exerciseService;

  public ExerciseController(ExerciseService exerciseService) {
    this.exerciseService = exerciseService;
  }

  @GetMapping
  public ResponseEntity<Page<Exercise>> getExercises(
      @PageableDefault(page = 0, size = 10)
          @SortDefault(sort = "name", direction = Sort.Direction.ASC)
          Pageable pageable) {
    var exercises = this.exerciseService.getExercises(pageable);
    return ResponseEntity.ok().body(exercises);
  }

  @PostMapping
  public ResponseEntity<ExerciseDTO> createExercise(@RequestBody @Valid ExerciseDTO exercise) {
    exercise.setCreatedBy("1fa67471-fc71-4a13-9d87-15d51f932bb2");
    var exerciseEntity = exercise.toEntity();
    var createdExercise = this.exerciseService.createExercise(exerciseEntity);
    return ResponseEntity.status(201).body(ExerciseDTO.fromEntity(createdExercise));
  }

  @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<List<ExerciseImageDTO>> uploadImages(
      @RequestParam(value = "images", required = true) @ValidExerciseImage
          List<@Valid MultipartFile> images) {

    var uploadedImages = this.exerciseService.storeMultipartFiles(images);
    var createdImages = uploadedImages.stream().map(this::setImageUrl).collect(Collectors.toList());
    return ResponseEntity.ok(createdImages);
  }

  private ExerciseImageDTO setImageUrl(String imageName) {
    var url = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + imageName).toUriString();
    return new ExerciseImageDTO(imageName, url);
  }

  @GetMapping(value = "/images/{name}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<FileSystemResource> downloadExerciseImage(
      @PathVariable(value = "name", required = true) String name) {
    var file = this.exerciseService.getFileFromImageDir(name);

    if (!file.exists()) {
      throw ApiException.fromApiError(
          HttpStatus.BAD_REQUEST, String.format("Image %s not found.", name));
    }

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }
}
