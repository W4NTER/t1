package ru.t1.controller;

import org.springframework.web.bind.annotation.*;
import ru.t1.dto.request.UserRequest;
import ru.t1.dto.response.UserResponse;
import ru.t1.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody UserRequest user) {
        return userService.create(user);
    }

    //Здесь id юзера в пути, потому, что не стал делать аутентификацию и авторизацию,
    // в здании они бесполезны, так я бы вытащил id из сессии например
    @PutMapping("/update/{user_id}")
    public UserResponse updateUser(
            @RequestBody UserRequest user,
            @PathVariable("user_id") Long userId) {
        return userService.update(user, userId);
    }

    @GetMapping("/{user_id}")
    public UserResponse getUser(@PathVariable("user_id") Long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{user_id}")
    public void deleteUser(@PathVariable("user_id") Long userId) {
        userService.delete(userId);
    }
}

