package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
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
            validation(user);
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
            validation(user);
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
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public User getUserById(Integer userId) throws NotFoundException {
        if (users.get(userId) == null) {
            throw new NotFoundException("Нет пользователя с таким логином");
        }
        return users.get(userId);
    }

    private void validation(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("email должен содержать @");
        if (user.getLogin().isBlank() || user.getLogin().contains(" "))
            throw new ValidationException("логин не должен содержать пробелов");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("нельзя родиться в будущем");
    }
}
