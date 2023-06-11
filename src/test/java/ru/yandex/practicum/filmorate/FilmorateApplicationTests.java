package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenresAndRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    private final GenresAndRatingDbStorage genresAndRatingDbStorage;
    Film film;
    User user1;
    User user2;
    Genre genre;
    Mpa mpa;


    @BeforeEach
    public void beforeEach() throws NotFoundException {
        mpa = Mpa.builder().id(1).build();
        genre = Genre.builder().id(1).build();
        film = Film.builder().name("Идеал").description("pfff").mpa(mpa).releaseDate(LocalDate.of(2000,
                        12, 28))
                .duration(1).build();
        film.setGenre(genre);
        user1 = User.builder().name("Идеал").email("i@mail.ru").login("user").birthday(LocalDate.of(2000, 12, 28)).build();
        user2 = User.builder().name("Идеал2").email("i@mail.ru").login("user2").birthday(LocalDate.of(2000, 12, 28)).build();
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        filmStorage.addFilm(film);
    }

    @Test
    public void testFindUserById() throws NotFoundException {

        User user = userStorage.getUserById(1);
        assertEquals(user.getId(), 1);

    }

    @Test
    public void updateUser() throws NotFoundException {
        user2.setName("Pfff");
        user2.setId(2);
        userStorage.updateUser(user2);
        User user = userStorage.getUserById(2);
        assertEquals(user.getName(), "Pfff");
    }

    @Test
    public void testAddToFriends() throws NotFoundException {
        userStorage.addToFriends(1, 2);
        User user = userStorage.getUserById(1);
        assertTrue(user.getFriends().contains(2));

    }

    @Test
    public void testDeleteFromFriends() throws NotFoundException {
        userStorage.addToFriends(1, 2);
        userStorage.deleteFromFriends(1, 2);
        User user = userStorage.getUserById(2);
        assertEquals(user.getFriends().size(), 0);
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(userStorage.getAll().size(), 8);
    }

    @Test
    public void testAddFilm() throws NotFoundException {
        Film film1 = filmStorage.getFilmById(1);
        assertEquals(film1.getId(), 1);
    }

    @Test
    public void testGetAllFilms() {
        assertEquals(filmStorage.getAllFilms().size(), 3);
    }

    @Test
    public void testAddLikeToFilm() throws NotFoundException {
        filmStorage.addLikeToFilm(1, 1);
        Film film1 = filmStorage.getFilmById(1);
        assertTrue(film1.getLikes().contains(1));
    }

    @Test
    public void testDeleteLikeFromFilm() throws NotFoundException {
        filmStorage.deleteLikeFromFilm(1, 1);
        Film film1 = filmStorage.getFilmById(1);
        assertEquals(film1.getLikes().size(), 0);
    }

    @Test
    public void testGetGenres() {
        assertTrue(genresAndRatingDbStorage.getAllGenres().size() > 0);
    }

    @Test
    public void testGetGenreById() throws NotFoundException {
        Genre genre = genresAndRatingDbStorage.getGenreById(1);
        assertEquals(genre.getId(), 1);
        assertNotNull(genre.getName());
    }

    @Test
    public void testGetALlRatings() {
        assertTrue(genresAndRatingDbStorage.getAllRatings().size() > 0);
    }

    @Test
    public void testGetRatingById() throws NotFoundException {
        Mpa mpa1 = genresAndRatingDbStorage.getRatingById(1);
        assertEquals(mpa1.getId(), 1);
        assertNotNull(mpa1.getName());
    }
}
