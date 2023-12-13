package recipes.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import recipes.exception.UserNotFoundException;
import recipes.model.UserDTO;
import recipes.service.UserService;


@Configuration
@AllArgsConstructor
@Log4j2
public class RecipeAuthProvider implements AuthenticationProvider {
    UserService userService;
    PasswordEncoderService passwordEncoderService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authenticating user: " + authentication.getName() + " with password: " + authentication.getCredentials());
        UserDTO userDTO;
        try {
            userDTO = userService.findByEmail(authentication.getName());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (passwordEncoderService.passwordEncoder().matches(authentication.getCredentials().toString(), userDTO.getPassword())) {
            log.info("User authenticated");
            return new RecipeAuth(authentication.getName(), authentication.getCredentials().toString(), true);
        }
        log.info("User not authenticated");
        throw new AuthenticationCredentialsNotFoundException("User not authenticated");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
