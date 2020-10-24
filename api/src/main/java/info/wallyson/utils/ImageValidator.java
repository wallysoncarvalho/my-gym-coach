package info.wallyson.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ValidImage, List<MultipartFile>> {

  @Override
  public boolean isValid(
      List<MultipartFile> listMultipartFile, ConstraintValidatorContext context) {

    for (var multipartFile : listMultipartFile) {
      var contentType = multipartFile.getContentType();
      if (!isSupportedContentType(contentType)) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(
                String.format(
                    "Only JPG and PNG images are allowed, %s provided.",
                    multipartFile.getContentType()))
            .addConstraintViolation();
        return false;
      }
      if (multipartFile.isEmpty()) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate("It must not be an empty image.")
            .addConstraintViolation();
        return false;
      }
    }

    return true;
  }

  private boolean isSupportedContentType(String contentType) {
    var supportedContents = List.of("image/jpg", "image/jpeg", "image/png");
    return supportedContents.contains(contentType);
  }
}
