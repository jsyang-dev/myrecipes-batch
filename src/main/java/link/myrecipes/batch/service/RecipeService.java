package link.myrecipes.batch.service;

public interface RecipeService {
    void deleteRecipeImage(String prefix);

    void deleteRecipeStepImage(String prefix);

    void makePopularRecipeList();
}
