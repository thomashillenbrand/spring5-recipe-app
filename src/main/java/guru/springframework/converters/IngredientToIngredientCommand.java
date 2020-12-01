package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand converter;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand converter) {
        this.converter = converter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {

        if (source == null) return null;
        final IngredientCommand command = new IngredientCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());
        command.setAmount(source.getAmount());
        command.setUom(converter.convert(source.getUom()));
        if (source.getRecipe() != null) command.setRecipeId(source.getRecipe().getId());
        return command;

    }
}
