package info.wallyson.service;

import info.wallyson.dto.ExerciseDTO;
import info.wallyson.entity.Exercise;
import info.wallyson.exception.ApiException;
import info.wallyson.repository.ExerciseRepository;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ExerciseService {
  private final ExerciseRepository exerciseRepository;
  private final String imageDir;

  public ExerciseService(
      ExerciseRepository exerciseRepository, @Value("${exercise.path}") final String imageDir) {
    this.exerciseRepository = exerciseRepository;
    this.imageDir = imageDir;
  }

  public Page<Exercise> getExercises(Pageable pageable) {
    return this.exerciseRepository.findAll(pageable);
  }

  public Exercise createExercise(ExerciseDTO exercise) {
    var exerciseExists = this.exerciseRepository.existsByName(exercise.getName());

    if (!exerciseExists) {
      return this.exerciseRepository.save(exercise.toEntity());
    }

    throw ApiException.fromApiError(
        HttpStatus.BAD_REQUEST,
        "Constraint error !",
        "An exercise with the name " + exercise.getName() + " already exists !");
  }

  public List<String> storeImages(List<MultipartFile> images) {
    // if list is empty return an empty list

    return images.stream()
        .map(this::storeFileOnLocation)
        .filter(Strings::isNotBlank)
        .collect(Collectors.toList());
  }

  private String storeFileOnLocation(MultipartFile multipartFile) {
    var name = "";

    try {
      name = UUID.randomUUID().toString();
      var path = Paths.get(this.imageDir).toAbsolutePath().normalize();
      Files.copy(multipartFile.getInputStream(), path.resolve(name));
    } catch (Exception e) {
      log.error("Error while saving multipartFile {}", multipartFile.getOriginalFilename());
    }

    return name;
  }
}
