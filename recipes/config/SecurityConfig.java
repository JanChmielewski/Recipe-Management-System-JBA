package recipes.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import recipes.service.UserService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UserService userService;
    RecipeAuthProvider recipeAuthProvider;
    PasswordEncoderService passwordEncoderService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoderService.passwordEncoder())
                .and().authenticationProvider(recipeAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and().csrf().disable().headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/api/register", "/actuator/shutdown", "/h2", "/h2/**").anonymous()
                .antMatchers("/api/recipe/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));
    }
}