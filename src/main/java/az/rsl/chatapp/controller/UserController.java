package az.rsl.chatapp.controller;

import az.rsl.chatapp.dto.UserDto;
import az.rsl.chatapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("{user}")
    public List<UserDto> get(@PathVariable(value = "user") Long userId) {
        return userService.get(userId);
    }
}
