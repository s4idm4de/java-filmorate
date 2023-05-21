package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User updateUser(User user) {
        try {
            return userStorage.updateUser(user);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        try {
            return userStorage.addUser(user);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void deleteUser(User user) {
        userStorage.deleteUser(user);
    }

    public User getUserById(Integer userId) {
        try {
            return userStorage.getUserById(userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void addToFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        if (userStorage.getUserById(user1Id) != null && userStorage.getUserById(user2Id) != null) {
            User user1 = userStorage.getUserById(user1Id);
            User user2 = userStorage.getUserById(user2Id);
            log.info("ДОБАВЛЕНИЕ В ДРУЗЬЯ {}", user1);
            user1.setFriends(user2.getId());
            user2.setFriends(user1.getId());
            log.info("ДОБАВЛЕНИЕ В ДРУЗЬЯ {}", user1.getFriends());
        } else if (userStorage.getUserById(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }


    public void deleteFromFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        User user1 = userStorage.getUserById(user1Id);
        User user2 = userStorage.getUserById(user2Id);
        if (user1 != null && user2 != null) {
            user1.getFriends().remove(user2.getId());
            user2.getFriends().remove(user1.getId());
        } else if (userStorage.getUserById(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }

    public List<User> getFriendsOfUser(Integer userId) throws NotFoundException {
        User user = userStorage.getUserById(userId);
        if (user != null) {
            return user.getFriends().stream().map(x -> {
                try {
                    return userStorage.getUserById(x);
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", userId));
        }
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) throws NotFoundException {
        if (userStorage.getUserById(user1Id) != null && userStorage.getUserById(user2Id) != null) {
            User user1 = userStorage.getUserById(user1Id);
            User user2 = userStorage.getUserById(user2Id);
            Set<Integer> intersectSet = new HashSet<>(user1.getFriends());
            intersectSet.retainAll(user2.getFriends());
            log.info("Число общих друзей {}", intersectSet.size());
            return intersectSet.stream().map(x -> {
                try {
                    return userStorage.getUserById(x);
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } else if (userStorage.getUserById(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }
}
