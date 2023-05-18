package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) throws NotFoundException {
        return inMemoryUserStorage.getUserById(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws NotFoundException, ValidationException {
        return inMemoryUserStorage.updateUser(user);
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
