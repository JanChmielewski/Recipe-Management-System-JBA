package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.exception.RecipeNotFoundException;
import recipes.exception.UnauthorizedException;
import recipes.mappers.RecipeMapper;
import recipes.model.RecipeDTO;
import recipes.repository.RecipeRepository;
import recipes.repository.entity.Recipe;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeService {

    @Autowired
    private final RecipeRepository recipeRepository;

    @Autowired
    private final RecipeMapper recipeMapper;

    public enum SearchParameter {
        CATEGORY, NAME
    }

    public List<RecipeDTO> search(SearchParameter searchParameter, String searchValue) {
        if (searchParameter == SearchParameter.CATEGORY) {
            return recipeMapper.mapToDTOList(
                    recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(searchValue));
        }
        return recipeMapper.mapToDTOList(
                recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(searchValue));
    }

    public RecipeDTO getRecipeById(Long id) throws RecipeNotFoundException {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            return recipeMapper.mapToDTO(optionalRecipe.get());
        } else {
            throw new RecipeNotFoundException("Recipe not found");
        }
    }

    public RecipeDTO saveRecipe(RecipeDTO recipe, Long authorId) throws IllegalArgumentException {
        if (recipe.getName() == null || recipe.getName().isBlank() ||
                recipe.getDescription() == null || recipe.getDescription().isBlank() ||
                recipe.getCategory() == null || recipe.getCategory().isBlank() ||
                recipe.getIngredients() == null || recipe.getIngredients().isEmpty() ||
                recipe.getDirections() == null || recipe.getDirections().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        recipe.setAuthorId(authorId);
        Recipe savedRecipe = recipeRepository.save(recipeMapper.mapToDB(recipe));
        return recipeMapper.mapToDTO(savedRecipe);
    }

    public void updateRecipe(Long id, RecipeDTO newRecipe, Long userId) throws IllegalArgumentException, UnauthorizedException, RecipeNotFoundException {
        if (newRecipe.getName() == null || newRecipe.getName().isBlank() ||
                newRecipe.getDescription() == null || newRecipe.getDescription().isBlank() ||
                newRecipe.getCategory() == null || newRecipe.getCategory().isBlank() ||
                newRecipe.getIngredients() == null || newRecipe.getIngredients().isEmpty() ||
                newRecipe.getDirections() == null || newRecipe.getDirections().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        } else if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException("Recipe not found");
        }

        Long authorId = recipeRepository.findById(id).get().getAuthorId();
        if (!authorId.equals(userId)) {
            throw new UnauthorizedException("You are not the author of this recipe");
        }
        Recipe recipeDB = recipeMapper.mapToDB(newRecipe);
        recipeDB.setId(id);
        recipeDB.setAuthorId(userId);
        recipeMapper.mapToDTO(recipeRepository.save(recipeDB));
    }

    public void deleteRecipeById(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        }
    }
}
