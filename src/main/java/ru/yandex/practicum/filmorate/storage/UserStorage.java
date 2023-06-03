package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User addUser(User user) throws NotFoundException;

    User updateUser(User user) throws NotFoundException;

    void deleteUser(User user);

    User getUserById(Integer userId) throws NotFoundException;
}
