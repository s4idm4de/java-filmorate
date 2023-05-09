package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Integer userId = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        validation(user);
        if (user.getId() == null) {
            user.setId(userId);
            userId++;
            users.put(user.getId(), user);
            log.info("Добавлен пользователь {}", user);
            return user;
        } else {
            throw new ValidationException("нельзя добавлять пользователя с id");
        }
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        validation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь {} обновлён", user);
            return user;
        } else {
            throw new ValidationException("нет ручек -- нет конфеток: пользователь должен быть в списке");
        }
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
