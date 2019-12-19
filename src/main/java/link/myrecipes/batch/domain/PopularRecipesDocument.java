package link.myrecipes.batch.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "popular_recipes")
@Getter
public class PopularRecipesDocument {
    @Id
    private Integer id;

    private String title;

    private List<String> recipeTagList;

    private Integer sequence;

    @Builder
    public PopularRecipesDocument(Integer id, String title, List<String> recipeTagList) {
        this.id = id;
        this.title = title;
        this.recipeTagList = recipeTagList;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
