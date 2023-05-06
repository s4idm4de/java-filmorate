package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Integer userId = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (validation(user)) {
            if (user.getId() == null) {
                user.setId(userId);
                userId++;
            }
            users.put(user.getId(), user);
            log.info("Добавлен пользователь {}", user);
            return user;
        } else {
            throw new ValidationException();
        }
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        if (validation(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь {} обновлён", user);
            return user;
        } else {
            throw new ValidationException();
        }
    }

    private boolean validation(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) return false;
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) return false;
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) return false;
        return true;
    }
}
