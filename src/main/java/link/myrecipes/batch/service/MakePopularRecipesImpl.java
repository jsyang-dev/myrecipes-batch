package link.myrecipes.batch.service;

import link.myrecipes.batch.domain.PopularRecipesDocument;
import link.myrecipes.batch.domain.RecipeEntity;
import link.myrecipes.batch.repository.PopularRecipesRepository;
import link.myrecipes.batch.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MakePopularRecipesImpl implements MakePopularRecipes {
    @Value("${app.popular-recipes.page-size}")
    private int pageSize;

    private final RecipeRepository recipeRepository;
    private final PopularRecipesRepository popularRecipesRepository;

    public MakePopularRecipesImpl(RecipeRepository recipeRepository, PopularRecipesRepository popularRecipesRepository) {
        this.recipeRepository = recipeRepository;
        this.popularRecipesRepository = popularRecipesRepository;
    }

    @Override
    @Transactional
    public void make() {
        // 조회수 기준 리스트 생성
        // TODO: 평점 기준으로 변경
        List<RecipeEntity> recipeEntityList = this.recipeRepository.findAllByOrderByReadCountDesc();
        log.info(recipeEntityList.toString());

//        recipeEntityList.sort(Comparator.comparing(RecipeEntity::getReadCount).reversed());
        List<PopularRecipesDocument> popularRecipesDocumentList = recipeEntityList
                .subList(0, (pageSize > recipeEntityList.size() ? recipeEntityList.size() : pageSize))
                .stream()
                .map(RecipeEntity::toDocument)
                .collect(Collectors.toList());

        int sequence = 1;
        for (PopularRecipesDocument popularRecipesDocument: popularRecipesDocumentList) {
            popularRecipesDocument.setSequence(sequence);
            sequence++;
        }

        this.popularRecipesRepository.deleteAll();
        this.popularRecipesRepository.saveAll(popularRecipesDocumentList);
        log.info(">>>>> make popular recipe list: {}", popularRecipesDocumentList.size());
    }
}
