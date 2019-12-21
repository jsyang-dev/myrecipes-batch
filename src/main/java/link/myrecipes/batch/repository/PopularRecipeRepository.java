package link.myrecipes.batch.repository;

import link.myrecipes.batch.domain.PopularRecipeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PopularRecipeRepository extends MongoRepository<PopularRecipeDocument, Integer> {
}
