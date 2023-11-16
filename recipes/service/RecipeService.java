package recipes.service;

import org.springframework.stereotype.Service;
import recipes.model.Recipe;

@Service
public class RecipeService {
    private Recipe storedRecipe;

    public void addRecipe(Recipe recipe) {
        storedRecipe = recipe;
    }

    public Recipe getRecipe() {
        return storedRecipe;
    }

}
