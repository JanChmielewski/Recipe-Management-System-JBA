package recipes.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Table
@Data
@Entity(name = "Recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String category;

    @ElementCollection
    private List<String> ingredients;

    @ElementCollection
    private List<String> directions;

    @Column
    private LocalDateTime date;

    @Column(nullable = false)
    private Long authorId;

}
