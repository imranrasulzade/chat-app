package az.rsl.chatapp.services;

import az.rsl.chatapp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();

    List<UserDto> get(Long userId, Optional<String> searchParam);
}
