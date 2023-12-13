package recipes.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.exception.RecipeNotFoundException;
import recipes.exception.UnauthorizedException;
import recipes.exception.UserNotFoundException;
import recipes.model.RecipeDTO;
import recipes.model.UserDTO;
import recipes.service.RecipeService;
import recipes.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRecipe(@RequestParam(required = false) Optional<String> category,
                                          @RequestParam(required = false) Optional<String> name) {
        if (category.isPresent() && name.isEmpty()) {
            return new ResponseEntity<>(recipeService.search(RecipeService.SearchParameter.CATEGORY, category.get()),
                    HttpStatus.OK);
        } else if (name.isPresent() && category.isEmpty()) {
            return new ResponseEntity<>(recipeService.search(RecipeService.SearchParameter.NAME, name.get()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        try {
            RecipeDTO recipe = recipeService.getRecipeById(id);
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        } catch (RecipeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> addRecipe(@AuthenticationPrincipal UserDTO auth, @RequestBody RecipeDTO recipe) {
        try {
            UserDTO user = userService.findByEmail(auth.getEmail());
            recipe.setDate(LocalDateTime.now());

            RecipeDTO savedRecipe = recipeService.saveRecipe(recipe, user.getId());
            return new ResponseEntity<>(Collections.singletonMap("id", savedRecipe.getId()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@AuthenticationPrincipal UserDTO auth, @PathVariable Long id, @Valid @RequestBody RecipeDTO newRecipe) {
        try {
            UserDTO user = userService.findByEmail(auth.getEmail());
            newRecipe.setDate(LocalDateTime.now());
            recipeService.updateRecipe(id, newRecipe, user.getId());
        } catch (RecipeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeById(@AuthenticationPrincipal UserDTO auth, @PathVariable Long id) {
        try {
            UserDTO user = userService.findByEmail(auth.getEmail());
            RecipeDTO recipe = recipeService.getRecipeById(id);
            if (recipe.getAuthorId().equals(user.getId())) {
                recipeService.deleteRecipeById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (RecipeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
