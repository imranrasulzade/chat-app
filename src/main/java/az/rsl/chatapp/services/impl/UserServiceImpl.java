package az.rsl.chatapp.services.impl;

import az.rsl.chatapp.repositories.UserRepository;
import az.rsl.chatapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUserName(username)
                        .orElseThrow(()-> new RuntimeException("User not found"));
//                        .orElseThrow(()-> new NotFoundException(User.class));
            }
        };
    }
}
