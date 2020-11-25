package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Enter getRecipes()");
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        log.debug("Exit getRecipes()");
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long id) {
        log.debug(String.format("Enter getRecipeById(%s)", id));
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) throw new RuntimeException("Recipe not found: " + id);
        log.debug(String.format("Exit getRecipeById(%s)", id));
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand getRecipeCommandById(Long id) {
        log.debug(String.format("Enter findRecipeCommandById(%s)", id));
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(getRecipeById(id));
        log.debug(String.format("Exit findRecipeCommandById(%s)", id));
        return recipeCommand;
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("Enter saveRecipeCommand()");
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe: " + savedRecipe.getId());
        log.debug("Exit saveRecipeCommand()");
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteRecipeById(Long id) {
        log.debug(String.format("Enter deleteRecipeById(%s)", id));
        recipeRepository.deleteById(id);
        log.debug(String.format("Exit deleteRecipeById(%s)", id));
    }
}
