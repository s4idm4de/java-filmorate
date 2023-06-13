package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Qualifier("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Integer userId = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) throws NotFoundException {
        try {
            Validation.userValidation(user);
            if (user.getId() == null) {
                user.setId(userId);
                userId++;
                users.put(user.getId(), user);
                log.info("Добавлен пользователь {}", user);
                return user;
            } else {
                throw new NotFoundException("нельзя добавлять пользователя с id");
            }
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        try {
            Validation.userValidation(user);
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Пользователь {} обновлён", user);
                return user;
            } else {
                throw new NotFoundException("нет ручек -- нет конфеток: пользователь должен быть в списке");
            }
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(Integer userId) throws NotFoundException {
        if (users.get(userId) == null) {
            throw new NotFoundException("Нет пользователя с таким логином");
        }
        return users.get(userId);
    }

    @Override
    public void addToFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        if (users.get(user1Id) != null && users.get(user2Id) != null) {
            User user1 = users.get(user1Id);
            User user2 = users.get(user2Id);
            log.info("ДОБАВЛЕНИЕ В ДРУЗЬЯ {}", user1);
            user1.setFriends(user2.getId());
            user2.setFriends(user1.getId());
            log.info("ДОБАВЛЕНИЕ В ДРУЗЬЯ {}", user1.getFriends());
        } else if (users.get(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }

    @Override
    public void deleteFromFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        User user1 = users.get(user1Id);
        User user2 = users.get(user2Id);
        if (user1 != null && user2 != null) {
            user1.getFriends().remove(user2.getId());
            user2.getFriends().remove(user1.getId());
        } else if (users.get(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }

}
