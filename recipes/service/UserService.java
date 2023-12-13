package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.api.model.AuthRequest;
import recipes.exception.UserNotFoundException;
import recipes.mappers.UserMapper;
import recipes.model.UserDTO;
import recipes.repository.UserRepository;
import recipes.repository.entity.User;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return findByEmail(email);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public UserDTO findByEmail(String email) throws UserNotFoundException {
        if (userRepository.existsByEmail(email)) {
            return userMapper.mapToDTO(userRepository.findByEmail(email).get());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void registerUser(AuthRequest request) throws IllegalArgumentException {
        if (request.getEmail() != null && request.getPassword() != null &&
                request.getPassword().length() >= 8 && !request.getPassword().isBlank() &&
                request.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(new User(request.getEmail(), request.getPassword()));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
