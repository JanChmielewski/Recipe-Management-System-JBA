package recipes.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import recipes.model.RecipeDTO;
import recipes.repository.entity.Recipe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class RecipeMapper {

    public Recipe mapToDB(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDTO.getId());
        recipe.setName(recipeDTO.getName());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setCategory(recipeDTO.getCategory());
        recipe.setIngredients(recipeDTO.getIngredients());
        recipe.setDirections(recipeDTO.getDirections());
        recipe.setDate(LocalDateTime.now());
        recipe.setAuthorId(recipeDTO.getAuthorId());
        return recipe;
    }

    public List<RecipeDTO> mapToDTOList(List<Recipe> dbRecipes) {
        List<RecipeDTO> dtoRecipe = new ArrayList<>();
        dbRecipes.forEach(recipe -> dtoRecipe.add(mapToDTO(recipe)));
        return dtoRecipe;
    }

    public RecipeDTO mapToDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());
        recipeDTO.setDescription(recipe.getDescription());
        recipeDTO.setCategory(recipe.getCategory());
        recipeDTO.setIngredients(recipe.getIngredients());
        recipeDTO.setDirections(recipe.getDirections());
        recipeDTO.setDate(recipe.getDate());
        recipeDTO.setAuthorId(recipe.getAuthorId());
        return recipeDTO;
    }
}
