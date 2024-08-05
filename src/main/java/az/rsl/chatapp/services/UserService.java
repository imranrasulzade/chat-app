package az.rsl.chatapp.services;

import az.rsl.chatapp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    List<UserDto> get(Long userId);
}
