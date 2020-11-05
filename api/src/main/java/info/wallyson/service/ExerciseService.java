package info.wallyson.service;

import info.wallyson.entity.Exercise;
import info.wallyson.exception.ApiException;
import info.wallyson.repository.ExerciseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

  public Exercise createExercise(Exercise exercise) {
    var exerciseExists = this.exerciseRepository.existsByName(exercise.getName());

    if (!exerciseExists) {
      return this.exerciseRepository.save(exercise);
    }

    throw ApiException.fromApiError(
        HttpStatus.BAD_REQUEST,
        "Constraint error !",
        List.of("An exercise with the name " + exercise.getName() + " already exists !"));
  }

  public List<String> storeMultipartFiles(List<MultipartFile> images) {
    return images.stream()
        .filter(mp -> !mp.isEmpty())
        .map(this::storeMultipartFileOnImageDir)
        .filter(Strings::isNotBlank)
        .collect(Collectors.toList());
  }

  private String storeMultipartFileOnImageDir(MultipartFile multipartFile) {
    var path = Paths.get(this.imageDir).toAbsolutePath().normalize();
    try {
      var name = getImageNewName(multipartFile);
      Files.copy(multipartFile.getInputStream(), path.resolve(name));
      return name;
    } catch (IOException e) {
      log.error("Error while saving file on path: {}", path.toString());
      throw ApiException.fromApiError(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "An error occurred on our servers while trying to save the images!");
    }
  }

  private String getImageNewName(MultipartFile multipartFile) {
    var originalFileName = multipartFile.getOriginalFilename();
    String extension = "";

    if (multipartFile.getContentType() != null) {
      extension = "." + multipartFile.getContentType().split("/")[1];
    } else if (originalFileName != null && originalFileName.isBlank()) {
      var splitName = originalFileName.split("\\.");
      extension = splitName.length > 1 ? "." + splitName[splitName.length - 1] : "";
    }

    return UUID.randomUUID().toString() + extension;
  }

  public FileSystemResource getFileFromImageDir(String fileName) {
    return new FileSystemResource(Path.of(this.imageDir, fileName));
  }
}
