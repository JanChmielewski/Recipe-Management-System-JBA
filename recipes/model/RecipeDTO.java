package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @UpdateTimestamp
    private LocalDateTime date;

    @NotBlank
    private String description;

    @NotEmpty
    private List<String> ingredients = new ArrayList<>();

    @NotEmpty
    private List<String> directions = new ArrayList<>();

    @JsonIgnore
    private Long authorId;
}
