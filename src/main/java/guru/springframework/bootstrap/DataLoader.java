package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Notes;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

@Component // since it implements command lin runer, when the app is started , it will run this method
public class DataLoader implements CommandLineRunner, ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        System.out.println("Enter onApplicationEvent");
        System.out.println(">>> EVENT: " + event.toString());
        System.out.println("Exit onApplicationEvent");

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Enter run");

        // Get Categories
        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");
        Optional<Category> fastFoodCategoryOptional = categoryRepository.findByDescription("Fast Food");

        if (mexicanCategoryOptional.isEmpty()) throw new RuntimeException("Expected Category not Found: Mexican");
        if (americanCategoryOptional.isEmpty()) throw new RuntimeException("Expected Category not Found: American");
        if (fastFoodCategoryOptional.isEmpty()) throw new RuntimeException("Expected Category not Found: Fast Food");

        Category mexCategory = mexicanCategoryOptional.get();
        Category americanCategory = americanCategoryOptional.get();
        Category fastFoodCategory = fastFoodCategoryOptional.get();

        // Get UnitsOfMeasure
        Optional<UnitOfMeasure> tspOptional = unitOfMeasureRepository.findByUom("Teaspoon");
        Optional<UnitOfMeasure> tbspOptional = unitOfMeasureRepository.findByUom("Tablespoon");
        Optional<UnitOfMeasure> dashOptional = unitOfMeasureRepository.findByUom("Dash");
        Optional<UnitOfMeasure> pintOptional = unitOfMeasureRepository.findByUom("Pint");
        Optional<UnitOfMeasure> cupOptional = unitOfMeasureRepository.findByUom("Cup");

        if (tspOptional.isEmpty()) throw new RuntimeException("Expected Unit of Measure not Found: Teaspoon");
        if (tbspOptional.isEmpty()) throw new RuntimeException("Expected Unit of Measure not Found: Tablespoon");
        if (dashOptional.isEmpty()) throw new RuntimeException("Expected Unit of Measure not Found: Dash");
        if (pintOptional.isEmpty()) throw new RuntimeException("Expected Unit of Measure not Found: Pint");
        if (cupOptional.isEmpty()) throw new RuntimeException("Expected Unit of Measure not Found: Cup");

        UnitOfMeasure tsp = tspOptional.get();
        UnitOfMeasure tbsp = tbspOptional.get();
        UnitOfMeasure dash = dashOptional.get();
        UnitOfMeasure pint = pintOptional.get();
        UnitOfMeasure cup = cupOptional.get();

        // GUACAMOLE RECIPE
        Recipe guacRecipe = new Recipe();
        guacRecipe.setSource("Simply Recipes");
        guacRecipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacRecipe.setImage(convertImageToByeArray(new URL("https://www.simplyrecipes.com/wp-content/uploads/2018/07/Guacamole-LEAD-1-600x840.jpg")));
        guacRecipe.setDescription("How to Make Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setServings(4);
        guacRecipe.setCookTime(null);
        guacRecipe.setDifficulty(Difficulty.EASY);
        StringBuilder directionsSB = new StringBuilder();
        directionsSB.append("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n\n");
        directionsSB.append("2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.) \n\n");
        directionsSB.append("3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n\n");
        directionsSB.append("4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");
        guacRecipe.setDirections(directionsSB.toString());

        guacRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), null));
        guacRecipe.addIngredient(new Ingredient("salt", new BigDecimal(.25), tsp));
        guacRecipe.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), tbsp));
        guacRecipe.addIngredient(new Ingredient("serrano chiles", new BigDecimal(1), null));
        guacRecipe.addIngredient(new Ingredient("cilantro", new BigDecimal(2), tbsp));
        guacRecipe.addIngredient(new Ingredient("black pepper", new BigDecimal(1), dash));
        guacRecipe.addIngredient(new Ingredient("ripe tomato", new BigDecimal(.5), null));
        guacRecipe.addIngredient(new Ingredient("red radish, garnish", new BigDecimal(1), null));
        guacRecipe.addIngredient(new Ingredient("tortilla chips", null, null));

        Notes guacNotes = new Notes(guacRecipe);
        guacNotes.setRecipeNotes("Guacamole has a role in the kitchen beyond a party dip, of course. " +
                "It’s great scooped on top of nachos and also makes an excellent topping or side for enchiladas, tacos, " +
                "grilled salmon, or oven-baked chicken. Guacamole is great in foods, as well. Try mixing some guacamole " +
                "into a tuna sandwich or your next batch of deviled eggs.");
        guacRecipe.setNotes(guacNotes);

        guacRecipe.getCategories().add(mexCategory);
        guacRecipe.getCategories().add(americanCategory);

        recipeRepository.save(guacRecipe);


        // CHICKEN TACOS RECIPE
        Recipe chickenTacoRecipe = new Recipe();
        chickenTacoRecipe.setSource("Simply Recipes");
        chickenTacoRecipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        chickenTacoRecipe.setImage(convertImageToByeArray(new URL("https://www.simplyrecipes.com/wp-content/uploads/2017/05/2017-05-29-GrilledChickenTacos-2.jpg")));
        chickenTacoRecipe.setDescription("How to Make Spicy Grilled Chicken Tacos");
        chickenTacoRecipe.setPrepTime(20);
        chickenTacoRecipe.setCookTime(15);
        chickenTacoRecipe.setServings(4);
        chickenTacoRecipe.setDifficulty(Difficulty.MODERATE);

        chickenTacoRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n");

        chickenTacoRecipe.addIngredient(new Ingredient("ancho chili powder", new BigDecimal(2), tbsp));
        chickenTacoRecipe.addIngredient(new Ingredient("dried oregano", new BigDecimal(1), tsp));
        chickenTacoRecipe.addIngredient(new Ingredient("dried cumin", new BigDecimal(1), tsp));
        chickenTacoRecipe.addIngredient(new Ingredient("sugar", new BigDecimal(1), tsp));
        chickenTacoRecipe.addIngredient(new Ingredient("salt", new BigDecimal(0.5), tsp));
        chickenTacoRecipe.addIngredient(new Ingredient("finely chopped garlic clove", new BigDecimal(1), null));
        chickenTacoRecipe.addIngredient(new Ingredient("orange zest", new BigDecimal(1), tbsp));
        chickenTacoRecipe.addIngredient(new Ingredient("orange juice", new BigDecimal(3), tbsp));
        chickenTacoRecipe.addIngredient(new Ingredient("olive oil", new BigDecimal(2), tbsp));
        chickenTacoRecipe.addIngredient(new Ingredient("skinless, boneless chicken thighs", new BigDecimal(4), null));
        chickenTacoRecipe.addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8), null));
        chickenTacoRecipe.addIngredient(new Ingredient("baby arugula", new BigDecimal(3), cup));
        chickenTacoRecipe.addIngredient(new Ingredient("medium ripe avocados", new BigDecimal(2), null));
        chickenTacoRecipe.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), null));
        chickenTacoRecipe.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(0.5), pint));
        chickenTacoRecipe.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(0.25), null));
        chickenTacoRecipe.addIngredient(new Ingredient("roughly chopped cilantro", null, null));
        chickenTacoRecipe.addIngredient(new Ingredient("sour cream", new BigDecimal(0.5), cup));
        chickenTacoRecipe.addIngredient(new Ingredient("milk", new BigDecimal(0.25), cup));
        chickenTacoRecipe.addIngredient(new Ingredient("lime", new BigDecimal(1), null));

        chickenTacoRecipe.getCategories().add(fastFoodCategory);
        chickenTacoRecipe.getCategories().add(mexCategory);

        Notes chickenTacoNotes = new Notes(chickenTacoRecipe);
        chickenTacoNotes.setRecipeNotes("marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.");
        chickenTacoRecipe.setNotes(chickenTacoNotes);

        recipeRepository.save(chickenTacoRecipe);

        System.out.println("Recipes loaded . . . ");
        System.out.println("Exit run");

    }

    public Byte[] convertImageToByeArray(URL imageUrl) throws IOException {

        Byte[] imageByteArrayObj = null;
        try {
            BufferedImage image = ImageIO.read(imageUrl);
            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", imageStream);
            byte[] imageByteArray = imageStream.toByteArray();

            imageByteArrayObj = new Byte[imageByteArray.length];
            Arrays.setAll(imageByteArrayObj, b -> imageByteArray[b]);

        } catch (IOException ex) {
            System.out.println("Unable to find image");
        }
        return imageByteArrayObj;
    }
}
