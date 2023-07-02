package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Qualifier("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) throws NotFoundException {
        try {
            Validation.filmValidation(film);
            if (film.getId() == null) {
                film.setId(filmId);
                filmId++;
                films.put(film.getId(), film);
                log.info("Добавлен фильм {}", film);
                return film;
            } else {
                throw new NotFoundException("you shell not pass с id");
            }
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        try {
            Validation.filmValidation(film);
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм {} обновлён", film);
                return film;
            } else {
                throw new NotFoundException("нельзя обновить то, у чего нет id");
            }
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    @Override
    public Film getFilmById(Integer filmId) throws NotFoundException {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else {
            throw new NotFoundException("нет фильма с таким id");
        }
    }

    @Override
    public void addLikeToFilm(Integer filmId, Integer userId) throws NotFoundException {
        if (films.get(filmId) != null) {
            log.info("ДОБАВЛЯЕМ ЛАЙК ФИЛЬМУ");
            films.get(filmId).getLikes().add(userId);
        } else if (films.get(filmId) == null) {
            throw new NotFoundException("нет фильма с таким id");
        } else {
            throw new NotFoundException("нет пользователя с таким id");
        }
    }

    @Override
    public void deleteLikeFromFilm(Integer filmId, Integer userId) throws NotFoundException {
        if (films.get(filmId) != null) {
            films.get(filmId).getLikes().remove(userId);
        } else if (films.get(filmId) == null) {
            throw new NotFoundException("нет фильма с таким id");
        } else {
            throw new NotFoundException("нет пользователя с таким id");
        }
    }
}
