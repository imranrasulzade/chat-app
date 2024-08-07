package az.rsl.chatapp.controller;

import az.rsl.chatapp.dto.UserDto;
import az.rsl.chatapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> get(@RequestParam(value = "user") Long userId,
                             @RequestParam Optional<String> searchParam) {
        return userService.get(userId, searchParam);
    }
}
