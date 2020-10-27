package info.wallyson.validations.exerciseimage;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExerciseImageTypeValidator.class})
public @interface ValidExerciseImage {
  String message() default "Invalid image file";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
