package guru.springframework.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // no cascading because if we delete the notes, we don't want to delete the recipe.
    private Recipe recipe;

    @Lob // will store in in clob field in database (bypass character limit)
    private String recipeNotes;

    public Notes() {
    }

    public Notes(Recipe recipe) {
        this.recipe = recipe;
    }

}
