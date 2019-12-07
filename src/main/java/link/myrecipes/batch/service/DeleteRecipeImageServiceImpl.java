package link.myrecipes.batch.service;

import link.myrecipes.batch.common.S3Helper;
import link.myrecipes.batch.domain.RecipeEntity;
import link.myrecipes.batch.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeleteRecipeImageServiceImpl implements DeleteRecipeImageService {
    private final RecipeRepository recipeRepository;
    private final S3Helper s3Helper;

    public DeleteRecipeImageServiceImpl(RecipeRepository recipeRepository, S3Helper s3Helper) {
        this.recipeRepository = recipeRepository;
        this.s3Helper = s3Helper;
    }

    @Override
    public void delete(String prefix) {
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
}
