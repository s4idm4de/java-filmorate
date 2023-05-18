package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage,
                          FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.filmService = filmService;
    }

    ;

    @GetMapping
    public List<Film> getAll() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException, NotFoundException {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, NotFoundException {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmId) throws NotFoundException {
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLikeToFilm(@PathVariable("userId") Integer userId,
                              @PathVariable("id") Integer filmId) throws NotFoundException {
        filmService.addLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable("id") Integer filmId,
                                   @PathVariable("userId") Integer userId) throws NotFoundException {
        filmService.deleteLikeFromFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopLikedFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return filmService.getTopFilms(10);
        } else {
            return filmService.getTopFilms(count);
        }
    }
}
