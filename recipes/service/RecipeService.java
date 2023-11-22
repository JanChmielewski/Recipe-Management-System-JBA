package recipes.service;

import org.springframework.stereotype.Service;
import recipes.model.Recipe;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecipeService {
    private final Map<Integer, Recipe> recipes = new HashMap<>();
    private int nextId = 1;

    public int addRecipe(Recipe recipe) {
        recipes.put(nextId, recipe);
        return nextId++;
    }

    public Recipe getRecipeById(int id) {
        return recipes.get(id);
    }
}
