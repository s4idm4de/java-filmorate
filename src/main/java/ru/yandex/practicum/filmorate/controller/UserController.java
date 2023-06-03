package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void putInFriendList(@PathVariable("id") Integer user1Id,
                                @PathVariable("friendId") Integer user2Id) throws NotFoundException {
        userService.addToFriends(user1Id, user2Id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriendship(@PathVariable("id") Integer user1Id,
                                     @PathVariable("friendId") Integer user2Id) throws NotFoundException {
        userService.deleteFromFriends(user1Id, user2Id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable("id") Integer userId) throws NotFoundException {
        return userService.getFriendsOfUser(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsOfUsers(@PathVariable("id") Integer user1Id,
                                              @PathVariable("otherId") Integer user2Id) throws NotFoundException {
        return userService.getCommonFriends(user1Id, user2Id);
    }
}
