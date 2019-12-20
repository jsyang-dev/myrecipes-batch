package link.myrecipes.batch.repository;

import link.myrecipes.batch.domain.PopularRecipesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PopularRecipesRepository extends MongoRepository<PopularRecipesDocument, Integer> {
}
