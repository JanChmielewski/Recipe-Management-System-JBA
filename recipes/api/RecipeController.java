package recipes.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.service.RecipeService;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<String> addRecipe(@RequestBody Recipe recipe) {
        recipeService.addRecipe(recipe);
        return ResponseEntity.ok("Recipe added successfully!");
    }

    @GetMapping
    public ResponseEntity<Recipe> getRecipe() {
        Recipe recipe = recipeService.getRecipe();
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
