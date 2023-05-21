package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    ;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmId) {
        return filmService.getFilmById(filmId);
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
