package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User addUser(User user) throws NotFoundException;

    User updateUser(User user) throws NotFoundException;


    User getUserById(Integer userId) throws NotFoundException;

    void addToFriends(Integer user1Id, Integer user2Id) throws NotFoundException;

    void deleteFromFriends(Integer user1Id, Integer user2Id) throws NotFoundException;
}
