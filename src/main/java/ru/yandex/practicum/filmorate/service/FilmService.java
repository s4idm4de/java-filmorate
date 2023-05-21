package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        if (filmStorage.getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
            log.info("ДОБАВЛЯЕМ ЛАЙК ФИЛЬМУ");
            filmStorage.getFilmById(filmId).getLikes().add(userId);
        } else if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("нет фильма с таким id");
        } else {
            throw new NotFoundException("нет пользователя с таким id");
        }
    }

    public void deleteLikeFromFilm(Integer filmId, Integer userId) throws NotFoundException {
        if (filmStorage.getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("нет фильма с таким id");
        } else {
            throw new NotFoundException("нет пользователя с таким id");
        }
    }

    public List<Film> getTopFilms(Integer count) {
        log.info("СПИСОК ЛУЧШИХ");
        return filmStorage.getAllFilms().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
