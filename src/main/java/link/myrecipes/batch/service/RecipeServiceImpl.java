package link.myrecipes.batch.service;

import link.myrecipes.batch.common.S3Helper;
import link.myrecipes.batch.domain.PopularRecipeDocument;
import link.myrecipes.batch.domain.RecipeEntity;
import link.myrecipes.batch.domain.RecipeStepEntity;
import link.myrecipes.batch.repository.PopularRecipeRepository;
import link.myrecipes.batch.repository.RecipeRepository;
import link.myrecipes.batch.repository.RecipeStepRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
    @Value("${app.popular-recipes.page-size}")
    private int pageSize;

    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final PopularRecipeRepository popularRecipesRepository;
    private final S3Helper s3Helper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeStepRepository recipeStepRepository, PopularRecipeRepository popularRecipesRepository, S3Helper s3Helper) {
        this.recipeRepository = recipeRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.popularRecipesRepository = popularRecipesRepository;
        this.s3Helper = s3Helper;
    }

    @Override
    @Transactional
    public void deleteRecipeImage(String prefix) {
        List<S3Object> s3ObjectList = this.s3Helper.listObjects(prefix);
        List<RecipeEntity> recipeEntityList = this.recipeRepository.findAllByOrderByImageAsc();
        List<String> deleteKeyList = new ArrayList<>();

        int s3ObjectIndex = 0;
        for (RecipeEntity recipeEntity : recipeEntityList) {
            for (int i = s3ObjectIndex; i < s3ObjectList.size(); i++) {
                String s3Image = s3ObjectList.get(i).key().replace(prefix + "/", "");
                if (s3Image.equals(recipeEntity.getImage())) {
                    s3ObjectIndex = i + 1;
                    break;
                }
                deleteKeyList.add(s3ObjectList.get(i).key());
            }
        }

        if (s3ObjectIndex < s3ObjectList.size()) {
            List<String> remainList = s3ObjectList.subList(s3ObjectIndex, s3ObjectList.size()).stream().map(S3Object::key).collect(Collectors.toList());
            deleteKeyList.addAll(remainList);
        }

        for (String deleteKey: deleteKeyList) {
            this.s3Helper.deleteObject(deleteKey);
            log.info(">>>>> delete s3 object complete: {}", deleteKey);
        }
    }

    @Override
    @Transactional
    public void deleteRecipeStepImage(String prefix) {
        List<S3Object> s3ObjectList = this.s3Helper.listObjects(prefix);
        List<RecipeStepEntity> recipeStepEntityList = this.recipeStepRepository.findByImageNotOrderByImageAsc("");
        List<String> deleteKeyList = new ArrayList<>();

        int s3ObjectIndex = 0;
        for (RecipeStepEntity recipeStepEntity : recipeStepEntityList) {
            for (int i = s3ObjectIndex; i < s3ObjectList.size(); i++) {
                String s3Image = s3ObjectList.get(i).key().replace(prefix + "/", "");
                if (s3Image.equals(recipeStepEntity.getImage())) {
                    s3ObjectIndex = i + 1;
                    break;
                }
                deleteKeyList.add(s3ObjectList.get(i).key());
            }
        }

        if (s3ObjectIndex < s3ObjectList.size()) {
            List<String> remainList = s3ObjectList.subList(s3ObjectIndex, s3ObjectList.size()).stream().map(S3Object::key).collect(Collectors.toList());
            deleteKeyList.addAll(remainList);
        }

        for (String deleteKey: deleteKeyList) {
            this.s3Helper.deleteObject(deleteKey);
            log.info(">>>>> delete s3 object complete: {}", deleteKey);
        }
    }

    @Override
    @Transactional
    public void makePopularRecipeList() {
        // 조회수 기준 리스트 생성
        // TODO: 평점 기준으로 변경
        List<RecipeEntity> recipeEntityList = this.recipeRepository.findAllByOrderByReadCountDesc();
        log.info(recipeEntityList.toString());

//        recipeEntityList.sort(Comparator.comparing(RecipeEntity::getReadCount).reversed());
        List<PopularRecipeDocument> popularRecipesDocumentList = recipeEntityList
                .subList(0, (pageSize > recipeEntityList.size() ? recipeEntityList.size() : pageSize))
                .stream()
                .map(RecipeEntity::toDocument)
                .collect(Collectors.toList());

        int sequence = 1;
        for (PopularRecipeDocument popularRecipesDocument: popularRecipesDocumentList) {
            popularRecipesDocument.setSequence(sequence);
            sequence++;
        }

        this.popularRecipesRepository.deleteAll();
        this.popularRecipesRepository.saveAll(popularRecipesDocumentList);
        log.info(">>>>> make popular recipe list: {}", popularRecipesDocumentList.size());
    }
}
