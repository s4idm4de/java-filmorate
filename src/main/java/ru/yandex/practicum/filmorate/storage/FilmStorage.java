package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film updateFilm(Film film) throws ValidationException, NotFoundException;

    Film addFilm(Film film) throws ValidationException, NotFoundException;

    void deleteFilm(Integer filmId) throws NotFoundException;

    List<Film> getAllFilms();

    Film getFilmById(Integer filmId) throws NotFoundException;
}
