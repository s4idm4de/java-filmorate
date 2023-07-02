package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestControllerAdvice
@RequestMapping
@Slf4j
public class GenresRatingController {
    private final FilmService filmService;

    @Autowired
    public GenresRatingController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable("id") Integer id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllRatings() {
        return filmService.getAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getRatingById(@PathVariable("id") Integer id) {
        return filmService.getRatingById(id);
    }
}
