package az.rsl.chatapp.services.impl;

import az.rsl.chatapp.dto.UserDto;
import az.rsl.chatapp.entities.Role;
import az.rsl.chatapp.entities.User;
import az.rsl.chatapp.repositories.RoleRepository;
import az.rsl.chatapp.repositories.UserRepository;
import az.rsl.chatapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUserName(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
//                        .orElseThrow(()-> new NotFoundException(User.class));
            }
        };
    }

    @Override
    public List<UserDto> get(Long userId, Optional<String> searchParam) {
        Optional<Role> role = roleRepository.findByName("USER");
        List<User> users = new ArrayList<>();
        if (role.isPresent()) {
            if (searchParam.isPresent()) {
                users = userRepository
                        .findUsersByIdIsNotAndStatusTrueAndRole_IdAndUserNameStartingWithIgnoreCaseOrderByUserNameAsc(userId,
                                role.get().getId(), searchParam.get());
            } else {
                users = userRepository
                        .findUsersByIdIsNotAndStatusTrueAndRole_IdOrderByUserNameAsc(userId, role.get().getId());
            }
        }
        if (!users.isEmpty()) {
            List<UserDto> userDtoList = Arrays.asList(modelMapper.map(users, UserDto[].class));
            return userDtoList;
        }
        return List.of();
    }
}
