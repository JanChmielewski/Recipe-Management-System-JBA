package recipes.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import recipes.model.UserDTO;
import recipes.repository.entity.User;


@Component
@AllArgsConstructor
public class UserMapper {

        public UserDTO mapToDTO(User user) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            return userDTO;
        }
}
