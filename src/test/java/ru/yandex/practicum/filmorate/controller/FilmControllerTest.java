package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film;
    FilmController filmController;

    @BeforeEach
    public void createItems() {
        film = Film.builder().name("Идеал").description("pfff").releaseDate(LocalDate.of(2000, 12, 28))
                .duration(1).build();
        filmController = new FilmController();
    }

    @Test
    public void noErrorWithCorrectFilm() throws ValidationException {
        Film film1 = filmController.create(film);
        assertEquals(1, film1.getId());
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    public void filmNameTest() {
        Film film1 = film.toBuilder().name("").build();
        Film film2 = film.toBuilder().name("  ").build();

        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film1);
            }
        });
        assertEquals("у фильма должно быть название", exception1.getMessage());

        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film2);
            }
        });
        assertEquals("у фильма должно быть название", exception2.getMessage());

        ValidationException exception3 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.update(film1);
            }
        });
        assertEquals("у фильма должно быть название", exception3.getMessage());

        ValidationException exception4 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.update(film2);
            }
        });
        assertEquals("у фильма должно быть название", exception4.getMessage());
    }

    @Test
    public void filmDescriptionTest() {
        String description = "";
        for (int i = 0; i < 201; i++) {
            description += "a";
        }

        Film film1 = film.toBuilder().description(description).build();
        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film1);
            }
        });
        assertEquals("сочинения свыше 200 символов никто читать не будет", exception1.getMessage());

        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.update(film1);
            }
        });
        assertEquals("сочинения свыше 200 символов никто читать не будет", exception2.getMessage());
    }

    @Test
    public void filmReleaseDateTest() {
        Film film1 = film.toBuilder().releaseDate(LocalDate.of(1895, 12, 27)).build();
        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film1);
            }
        });
        assertEquals("курица или яйцо? сначала изобрели камеру, а потом начали снимать фильмы. " +
                "Никаких фильмов до 28 декабря 1895 года!", exception1.getMessage());
        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.update(film1);
            }
        });
        assertEquals("курица или яйцо? " +
                "сначала изобрели камеру, а потом начали снимать фильмы. Никаких фильмов до 28 декабря 1895 года!", exception2.getMessage());
    }

    @Test
    public void filmDurationTest() {
        Film film1 = film.toBuilder().duration(-1).build();
        ValidationException exception1 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.create(film1);
            }
        });
        assertEquals("фильм должен длиться хоть сколько-то", exception1.getMessage());

        ValidationException exception2 = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.update(film1);
            }
        });
        assertEquals("фильм должен длиться хоть сколько-то", exception2.getMessage());
    }
}