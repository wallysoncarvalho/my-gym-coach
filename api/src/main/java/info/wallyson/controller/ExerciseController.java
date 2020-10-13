package info.wallyson.controller;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.dto.ExerciseImageDTO;
import info.wallyson.entity.Exercise;
import info.wallyson.service.ExerciseService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/v1/exercises", produces = MediaType.APPLICATION_JSON_VALUE)
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
  public ResponseEntity<Exercise> createExercise(@RequestBody @Valid ExerciseDTO exercise) {
    // get the id of the user from security context
    exercise.setCreatedBy("1fa67471-fc71-4a13-9d87-15d51f932bb2");

    var createdExercise = this.exerciseService.createExercise(exercise);
    return ResponseEntity.status(201).body(createdExercise);
  }

  @PostMapping(value = "images")
  public ResponseEntity<List<ExerciseImageDTO>> uploadImages(
      @RequestParam(value = "images", required = true) List<MultipartFile> images) {

    // test list size and return 400 if empty
    // test files extension and return 400 if not image

    var uploadedImages = this.exerciseService.storeImages(images);
    var createdImages = uploadedImages.stream().map(this::setImageUrl).collect(Collectors.toList());
    return ResponseEntity.ok(createdImages);
  }

  private ExerciseImageDTO setImageUrl(String imageName) {
    var url = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + imageName).toUriString();
    return new ExerciseImageDTO(imageName, url);
  }
}