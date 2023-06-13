package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film updateFilm(Film film) throws NotFoundException;

    Film addFilm(Film film) throws NotFoundException;

    List<Film> getAllFilms();

    Film getFilmById(Integer filmId) throws NotFoundException;

    void addLikeToFilm(Integer filmId, Integer userId) throws NotFoundException;

    void deleteLikeFromFilm(Integer filmId, Integer userId) throws NotFoundException;
}
