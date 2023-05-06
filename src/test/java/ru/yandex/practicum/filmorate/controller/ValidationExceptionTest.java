package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {
    User user;
    UserController userController;
    Film film;
    FilmController filmController;

    @BeforeEach
    public void createItems(){
        String userDate = "2000-12-28T00:00:00Z";
        user = User.builder().name("Идеал").email("i@mail.ru").login("user").birthday(LocalDate.of(2000, 12, 28)).build();
        userController = new UserController();
        film = Film.builder().name("Идеал").description("pfff").releaseDate(LocalDate.of(2000, 12, 28))
                .duration(1).build();
        filmController = new FilmController();
    }

    @Test
    public void noErrorWithCorrectUser() throws ValidationException{
        assertEquals(user, userController.create(user));
        assertEquals(user, userController.update(user));
    }

    @Test
    public void userLoginTest(){
        User user1 = user.toBuilder().login("").build();
        User user2 = user.toBuilder().login("  ").build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.create(user1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.create(user2);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.update(user1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.update(user2);}
        });
    }

    @Test
    public void userEmailTest(){
        User user1 = user.toBuilder().email("  ").build();
        User user2 = user.toBuilder().email("pffff").build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.create(user1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.create(user2);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.update(user1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.update(user2);}
        });
    }

    @Test
    public void userBirthdayTest(){
        User user1 = user.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.create(user1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { userController.update(user1);}
        });
    }

    @Test
    public void userNameTest() throws ValidationException {
        User user1 = user.toBuilder().name("").build();
        assertEquals(user1.getLogin(), userController.create(user1).getName());
        assertEquals(user1.getLogin(), userController.update(user1).getName());
    }

    @Test
    public void noErrorWithCorrectFilm() throws ValidationException {
        assertEquals(film, filmController.create(film));
        assertEquals(film, filmController.update(film));
    }

    @Test
    public void filmNameTest(){
        Film film1 = film.toBuilder().name("").build();
        Film film2 = film.toBuilder().name("  ").build();

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.create(film1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.create(film2);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.update(film1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.update(film2);}
        });
    }

    @Test
    public void filmDescriptionTest(){
        String description = "";
        for (int i = 0; i<201; i++){
            description+="a";
        }

        Film film1 = film.toBuilder().description(description).build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.create(film1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.update(film1);}
        });
    }

    @Test
    public void filmReleaseDateTest(){
        String filmDate = "1895-12-27T00:00:00Z";
        Film film1 = film.toBuilder().releaseDate(LocalDate.of(1895, 12, 27)).build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.create(film1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.update(film1);}
        });
    }

    @Test
    public void filmDurationTest(){
        Film film1 = film.toBuilder().duration(-1).build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.create(film1);}
        });

        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException { filmController.update(film1);}
        });
    }
}