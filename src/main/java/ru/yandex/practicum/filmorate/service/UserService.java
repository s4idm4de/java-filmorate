package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
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
        if (userStorage.getUserById(user1Id) != null && userStorage.getUserById(user2Id) != null) {
            User user1 = userStorage.getUserById(user1Id);
            User user2 = userStorage.getUserById(user2Id);
            user1.getFriends().remove(user2.getId());
            user2.getFriends().remove(user1.getId());
        } else if (userStorage.getUserById(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }

    public List<User> getFriendsOfUser(Integer userId) throws NotFoundException {
        if (userStorage.getUserById(userId) != null) {
            List<User> friends = new ArrayList<>();
            for (Integer id : userStorage.getUserById(userId).getFriends()) {
                friends.add(userStorage.getUserById(id));
            }
            return friends;
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
            List<User> commonFriends = new ArrayList<>();
            for (Integer userId : intersectSet) {
                commonFriends.add(userStorage.getUserById(userId));
            }
            log.info("БЕРЁМ ОБЩИХ ДРУЗЕЙ");
            return commonFriends;
        } else if (userStorage.getUserById(user1Id) == null) {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user1Id));
        } else {
            throw new NotFoundException(String.format("Нет пользователя с id {}", user2Id));
        }
    }
}
