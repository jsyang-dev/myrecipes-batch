package link.myrecipes.batch.repository;

import link.myrecipes.batch.domain.RecipeStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeStepEntity, Integer> {
    List<RecipeStepEntity> findByImageNotOrderByImageAsc(String s);
}
