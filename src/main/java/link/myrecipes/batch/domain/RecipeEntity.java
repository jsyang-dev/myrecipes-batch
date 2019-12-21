package link.myrecipes.batch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "recipe")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "recipeTagEntityList")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class RecipeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer estimatedTime;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(nullable = false)
    private Integer readCount;

    @OneToMany(mappedBy = "recipeEntity", cascade = CascadeType.ALL)
    private List<RecipeTagEntity> recipeTagEntityList = new ArrayList<>();

    @Builder
    public RecipeEntity(String title, String image, Integer estimatedTime, Integer difficulty, Integer readCount) {
        this.title = title;
        this.image = image;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.readCount = readCount;
    }

    public void addRecipeTag(RecipeTagEntity recipeTagEntity) {
        this.recipeTagEntityList.add(recipeTagEntity);
    }

    public PopularRecipeDocument toDocument() {
        return PopularRecipeDocument.builder()
                .id(this.getId())
                .title(this.getTitle())
                .image(this.getImage())
                .readCount(this.readCount)
                .recipeTagList(this.getRecipeTagEntityList().stream().map(RecipeTagEntity::getTag).collect(Collectors.toList()))
                .build();
    }
}
