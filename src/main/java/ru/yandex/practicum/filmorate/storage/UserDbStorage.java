package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sqlUsers = "select * from users";
        List<User> users = jdbcTemplate.query(sqlUsers, (userRows, rowNum) -> User.builder().id(userRows.getInt("id")).name(userRows.getString("name"))
                .email(userRows.getString("email")).login(userRows.getString("login"))
                .birthday(userRows.getDate("birthday").toLocalDate()).build());
        users.stream().map(user -> addFriends(user));
        return users;
    }

    @Override
    public User addUser(User user) throws NotFoundException {
        try {
            validation(user);
            String sqlQuery = "insert into users(email, name, login, birthday) " +
                    "values (?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getName(),
                    user.getLogin(),
                    user.getBirthday());
            SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select count(*) as last_id from users");
            sqlRows.next();
            user.setId(sqlRows.getInt("last_id"));
            log.info("Добавление пользователя {}", user.getId());
            return user;
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select id from users where id = ?", user.getId());

        if (sqlRows.next()) {


            log.info("ПОЛЬЗОВАТЕЛЬ {} ОБНОВЛЕН", sqlRows.getInt("id"));
            String sqlQuery = "update users set " +
                    "name = ?, login = ?, birthday = ? , email = ?" +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getName()
                    , user.getLogin()
                    , user.getBirthday()
                    , user.getEmail()
                    , user.getId());
            return user;

        } else {
            log.info("НЕТ ПОЛЬЗОВАТЕЛЯ {} ", user.getId());
            throw new NotFoundException("нет такого пользователя");
        }

    }

    @Override
    public void deleteUser(User user) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public User getUserById(Integer userId) throws NotFoundException {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", userId);
        if (userRows.next()) {
            User user = User.builder().id(userRows.getInt("id")).name(userRows.getString("name"))
                    .email(userRows.getString("email")).login(userRows.getString("login"))
                    .birthday(userRows.getDate("birthday").toLocalDate()).build();
            return addFriends(user);
        } else {
            throw new NotFoundException("нет такого пользователя");
        }
    }

    @Override
    public void addToFriends(Integer user1Id, Integer user2Id) {
        try {
            getUserById(user1Id);
            getUserById(user2Id);
            String sqlQuery = "insert into friends(user_id_from, user_id_to) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    user1Id,
                    user2Id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    public void deleteFromFriends(Integer user1Id, Integer user2Id) {
        String sqlQuery = "delete from friends where user_id_from = ? and user_id_to = ?";
        jdbcTemplate.update(sqlQuery,
                user1Id,
                user2Id);
    }

    private User addFriends(User user) {
        String sqlFriends = "SELECT user_id_to FROM friends WHERE user_id_from = ? GROUP BY user_id_to";
        List<Integer> users = jdbcTemplate.query(sqlFriends, (rs, rowNum) -> rs.getInt("user_id_to"), user.getId());
        for (Integer id : users) {
            user.setFriends(id);
        }
        return user;
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
