package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.repository.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
    boolean existsByEmail(String email);

    User save(User newUser);
}
