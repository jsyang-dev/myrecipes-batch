package link.myrecipes.batch.repository;

import link.myrecipes.batch.domain.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {
    List<RecipeEntity> findAllByOrderByImageAsc();
}
