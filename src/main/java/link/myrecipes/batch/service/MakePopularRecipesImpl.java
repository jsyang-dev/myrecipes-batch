package link.myrecipes.batch.service;

import link.myrecipes.batch.domain.PopularRecipesDocument;
import link.myrecipes.batch.domain.RecipeEntity;
import link.myrecipes.batch.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MakePopularRecipesImpl implements MakePopularRecipes {
    private final RecipeRepository recipeRepository;

    @Value("${app.popular-recipes.page-size}")
    private int pageSize;

    public MakePopularRecipesImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void make() {
        List<RecipeEntity> recipeEntityList = this.recipeRepository.findAll();

        // 조회수 기준 리스트 생성
        // TODO: 평점 기준으로 변경
        recipeEntityList.sort(Comparator.comparing(RecipeEntity::getReadCount));
        List<PopularRecipesDocument> popularRecipesDocumentList = recipeEntityList.subList(0, pageSize)
                .stream()
                .map(RecipeEntity::toDocument)
                .collect(Collectors.toList());

        int sequence = 1;
        for (PopularRecipesDocument popularRecipesDocument: popularRecipesDocumentList) {
            popularRecipesDocument.setSequence(sequence);
            sequence++;
        }

        log.info(popularRecipesDocumentList.toString());
    }
}
