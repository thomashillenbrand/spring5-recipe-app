package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 UnitOfMeasureRepository unitOfMeasureRepository,
                                 RecipeRepository recipeRepository) {

        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if(recipeOptional.isEmpty()) {
            log.error("Recipe not found for ID: "+recipeId);
            // TODO: implement error handling
        }

        Recipe targetRecipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = targetRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(ingredientCommandOptional.isEmpty()) {
            log.error("Ingredient not found for ID: "+ingredientId);
            // TODO: implement error handling
        }

        return ingredientCommandOptional.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
        if(recipeOptional.isEmpty()) {
            log.error("Recipe not found for id: "+command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) { //updating existing ingredient from recipe
                Ingredient foundIngredient = ingredientOptional.get();
                foundIngredient.setDescription(command.getDescription());
                foundIngredient.setAmount(command.getAmount());
                if(command.getUom().getId() != null) {
                    foundIngredient.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                            .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
                } else foundIngredient.setUom(null);

            } else {
                if(command.getUom().getId() == null) command.setUom(null); // figure out what is happening here --> debug mode
                recipe.addIngredient(ingredientCommandToIngredient.convert(command)); // add ingredient to recipe
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
                            .stream()
                            .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                            .findFirst();

            if(savedIngredientOptional.isEmpty()) {
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription())).findFirst();
            }
             return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }

    }
}
