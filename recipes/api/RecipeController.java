package recipes.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/recipe")
@Validated
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> addRecipe(@Valid @RequestBody Recipe recipe) {
        Long recipeId = recipeService.saveOrUpdate(recipe);

        Map<String, Long> responseBody = new HashMap<>();
        responseBody.put("id", recipeId);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Recipe>> getRecipe(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeById(@Valid @PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            recipeService.deleteRecipeById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        Optional<Recipe> existingRecipe = recipeService.getRecipeById(id);

        if (existingRecipe.isPresent()) {
            Recipe recipeToUpdate = existingRecipe.get();
            recipeToUpdate.setName(recipe.getName());
            recipeToUpdate.setCategory(recipe.getCategory());
            recipeToUpdate.setDate(LocalDateTime.now());
            recipeToUpdate.setDescription(recipe.getDescription());
            recipeToUpdate.setIngredients(recipe.getIngredients());
            recipeToUpdate.setDirections(recipe.getDirections());

            recipeService.saveOrUpdate(recipeToUpdate);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRecipe(@RequestParam(required = false) String category,
                                          @RequestParam(required = false) String name) {
        if (category != null && name != null) {
            return ResponseEntity.badRequest().build();
        } else if (category != null) {
            List<Recipe> recipes = recipeService.searchByCategory(category.toLowerCase());
            return ResponseEntity.ok(recipes.isEmpty() ? Collections.emptyList() : recipes);
        } else if (name != null) {
            List<Recipe> recipes = recipeService.searchByName(name.toLowerCase());
            return ResponseEntity.ok(recipes.isEmpty() ? Collections.emptyList() : recipes);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
