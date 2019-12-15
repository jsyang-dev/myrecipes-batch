package link.myrecipes.batch.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recipe_step")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RecipeStepEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer step;

    @Column(nullable = false)
    private String content;

    private String image;

    @Builder
    public RecipeStepEntity(Integer step, String content, String image) {
        this.step = step;
        this.content = content;
        this.image = image;
    }
}
