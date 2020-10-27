package info.wallyson.validations.exerciseimage;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExerciseImageTypeValidator
    implements ConstraintValidator<ValidExerciseImage, List<MultipartFile>> {

  public final long IMG_MAX_BYTE_SIZE = 1024 * 1024; // 1MB

  @Override
  public boolean isValid(List<MultipartFile> multipartFiles, ConstraintValidatorContext context) {

    for (var multipartFile : multipartFiles) {
      var valMsg = getValidationMessage(multipartFile);

      if (!valMsg.isBlank()) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(valMsg).addConstraintViolation();
        return false;
      }
    }

    return true;
  }

  private String getValidationMessage(MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      return "It must not be an empty image.";
    } else if (multipartFile.getSize() > IMG_MAX_BYTE_SIZE) {
      return "File size should be at most 1MB";
    } else if (!isSupportedContentType(multipartFile)) {
      return "Only JPG and PNG images are allowed.";
    }

    return "";
  }

  private boolean isSupportedContentType(MultipartFile multipartFile) {
    var supportedContents = List.of("image/jpg", "image/jpeg", "image/png");
    return supportedContents.contains(multipartFile.getContentType());
  }
}
