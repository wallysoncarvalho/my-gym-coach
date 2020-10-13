package info.wallyson.repository;

import info.wallyson.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, String> {

  boolean existsByName(String exercise);
}
