package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    User user;
    UserController userController;

    @BeforeEach
    public void createItems() {
        user = User.builder().name("Идеал").email("i@mail.ru").login("user").birthday(LocalDate.of(2000, 12, 28)).build();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        UserService userService = new UserService(inMemoryUserStorage);
        userController = new UserController(inMemoryUserStorage, userService);
    }

    @Test
    public void noErrorWithCorrectUser() throws ValidationException {
        User user1 = userController.create(user);
        assertEquals(1, user1.getId());
        assertEquals(1, userController.getAll().size());
    }

    @Test
    public void userLoginTest() {
        User user1 = user.toBuilder().login("").build();
        User user2 = user.toBuilder().login("  ").build();
        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.create(user1);
            }
        });

        assertEquals("логин не должен содержать пробелов", exception1.getMessage());

        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.create(user2);
            }
        });

        assertEquals("логин не должен содержать пробелов", exception2.getMessage());
        ValidationException exception3 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException, NotFoundException {
                userController.update(user1);
            }
        });

        assertEquals("логин не должен содержать пробелов", exception3.getMessage());
        ValidationException exception4 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException, NotFoundException {
                userController.update(user2);
            }
        });

        assertEquals("логин не должен содержать пробелов", exception4.getMessage());
    }

    @Test
    public void userEmailTest() {
        User user1 = user.toBuilder().email("  ").build();
        User user2 = user.toBuilder().email("pffff").build();
        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.create(user1);
            }
        });

        assertEquals("email должен содержать @", exception1.getMessage());
        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.create(user2);
            }
        });

        assertEquals("email должен содержать @", exception2.getMessage());
        ValidationException exception3 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException, NotFoundException {
                userController.update(user1);
            }
        });

        assertEquals("email должен содержать @", exception3.getMessage());
        ValidationException exception4 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException, NotFoundException {
                userController.update(user2);
            }
        });
        assertEquals("email должен содержать @", exception4.getMessage());
    }

    @Test
    public void userBirthdayTest() {
        User user1 = user.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.create(user1);
            }
        });
        assertEquals("нельзя родиться в будущем", exception1.getMessage());
        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException, NotFoundException {
                userController.update(user1);
            }
        });
        assertEquals("нельзя родиться в будущем", exception2.getMessage());
    }

    @Test
    public void userNameTest() throws ValidationException, NotFoundException {
        User user1 = user.toBuilder().name("").build();
        assertEquals(user1.getLogin(), userController.create(user1).getName());
        assertEquals(user1.getLogin(), userController.update(user1).getName());
    }

}