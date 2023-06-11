package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenresAndRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    @Autowired
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Autowired
    private final GenresAndRatingDbStorage genresAndRatingDbStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage,
                       GenresAndRatingDbStorage genresAndRatingDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genresAndRatingDbStorage = genresAndRatingDbStorage;
    }

    public Film addFilm(Film film) {
        try {
            return filmStorage.addFilm(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public Film updateFilm(Film film) {
        try {
            return filmStorage.updateFilm(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void deleteFilm(Integer filmId) {
        try {
            filmStorage.deleteFilm(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        try {
            return filmStorage.getFilmById(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void addLikeToFilm(Integer filmId, Integer userId) throws NotFoundException {
        if (userStorage.getUserById(userId) != null) {
            filmStorage.addLikeToFilm(filmId, userId);
        } else {
            throw new NotFoundException("нет пользователя с таким id");
        }
    }

    public void deleteLikeFromFilm(Integer filmId, Integer userId) {
        try {
            userStorage.getUserById(userId);
            filmStorage.deleteLikeFromFilm(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<Film> getTopFilms(Integer count) {
        log.info("СПИСОК ЛУЧШИХ");
        return filmStorage.getAllFilms().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Genre> getAllGenres() {
        return genresAndRatingDbStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        try {
            return genresAndRatingDbStorage.getGenreById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<Mpa> getAllRatings() {
        return genresAndRatingDbStorage.getAllRatings();
    }

    public Mpa getRatingById(Integer id) {
        try {
            return genresAndRatingDbStorage.getRatingById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
