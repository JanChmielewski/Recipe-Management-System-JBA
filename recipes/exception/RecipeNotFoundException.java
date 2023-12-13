package recipes.exception;

public class RecipeNotFoundException extends Throwable {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
