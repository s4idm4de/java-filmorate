package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.web.server.ResponseStatusException;
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
        userController = new UserController(userService);
    }

    @Test
    public void noErrorWithCorrectUser() {
        User user1 = userController.create(user);
        assertEquals(1, user1.getId());
        assertEquals(1, userController.getAll().size());
    }

    @Test
    public void userLoginTest() {
        User user1 = user.toBuilder().login("").build();
        User user2 = user.toBuilder().login("  ").build();
        ResponseStatusException exception1 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws ResponseStatusException {
                userController.create(user1);
            }
        });

        assertEquals("400 BAD_REQUEST \"логин не должен содержать пробелов\"; nested exception " +
                "is ru.yandex.practicum.filmorate.exception.ValidationException:" +
                " логин не должен содержать пробелов", exception1.getMessage());

        ResponseStatusException exception2 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() throws ResponseStatusException {
                userController.create(user2);
            }
        });

        assertEquals("400 BAD_REQUEST \"логин не должен содержать пробелов\"; nested exception " +
                "is ru.yandex.practicum.filmorate.exception.ValidationException:" +
                " логин не должен содержать пробелов", exception2.getMessage());
        ResponseStatusException exception3 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.update(user1);
            }
        });

        assertEquals("400 BAD_REQUEST \"логин не должен содержать пробелов\"; nested exception " +
                "is ru.yandex.practicum.filmorate.exception.ValidationException:" +
                " логин не должен содержать пробелов", exception3.getMessage());
        ResponseStatusException exception4 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.update(user2);
            }
        });

        assertEquals("400 BAD_REQUEST \"логин не должен содержать пробелов\"; nested exception " +
                "is ru.yandex.practicum.filmorate.exception.ValidationException:" +
                " логин не должен содержать пробелов", exception4.getMessage());
    }

    @Test
    public void userEmailTest() {
        User user1 = user.toBuilder().email("  ").build();
        User user2 = user.toBuilder().email("pffff").build();
        ResponseStatusException exception1 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.create(user1);
            }
        });

        assertEquals("400 BAD_REQUEST \"email должен содержать @\"; nested exception is " +
                "ru.yandex.practicum.filmorate.exception.ValidationException: " +
                "email должен содержать @", exception1.getMessage());
        ResponseStatusException exception2 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.create(user2);
            }
        });

        assertEquals("400 BAD_REQUEST \"email должен содержать @\"; nested exception is ru.yandex" +
                ".practicum.filmorate.exception.ValidationException: " +
                "email должен содержать @", exception2.getMessage());
        ResponseStatusException exception3 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.update(user1);
            }
        });

        assertEquals("400 BAD_REQUEST \"email должен содержать @\"; nested exception" +
                " is ru.yandex.practicum.filmorate.exception.ValidationException: " +
                "email должен содержать @", exception3.getMessage());
        ResponseStatusException exception4 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.update(user2);
            }
        });
        assertEquals("400 BAD_REQUEST \"email должен содержать @\"; nested exception is" +
                " ru.yandex.practicum.filmorate.exception.ValidationException: " +
                "email должен содержать @", exception4.getMessage());
    }

    @Test
    public void userBirthdayTest() {
        User user1 = user.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        ResponseStatusException exception1 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.create(user1);
            }
        });
        assertEquals("400 BAD_REQUEST \"нельзя родиться в будущем\"; nested exception is" +
                " ru.yandex.practicum.filmorate.exception.ValidationException:" +
                " нельзя родиться в будущем", exception1.getMessage());
        ResponseStatusException exception2 = assertThrows(ResponseStatusException.class, new Executable() {
            @Override
            public void execute() {
                userController.update(user1);
            }
        });
        assertEquals("400 BAD_REQUEST \"нельзя родиться в будущем\"; nested exception is" +
                " ru.yandex.practicum.filmorate.exception." +
                "ValidationException: нельзя родиться в будущем", exception2.getMessage());
    }

    @Test
    public void userNameTest() {
        User user1 = user.toBuilder().name("").build();
        assertEquals(user1.getLogin(), userController.create(user1).getName());
        assertEquals(user1.getLogin(), userController.update(user1).getName());
    }

}