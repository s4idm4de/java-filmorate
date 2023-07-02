package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class GenresAndRatingDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenresAndRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(Integer id) throws NotFoundException {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from genres_names where genre_id = ?", id);
        if (sqlRows.next()) {
            return Genre.builder().name(sqlRows.getString("genre_name"))
                    .id(sqlRows.getInt("genre_id")).build();
        } else {
            throw new NotFoundException("нет такого жанра");
        }
    }

    public List<Genre> getAllGenres() {
        String sql = "select * from genres_names";
        List<Genre> genres = jdbcTemplate.query(sql, (sqlRows, rowNum) -> Genre.builder()
                .name(sqlRows.getString("genre_name"))
                .id(sqlRows.getInt("genre_id")).build());
        return genres;
    }

    public Mpa getRatingById(Integer id) throws NotFoundException {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from mpas where rating_id = ?", id);
        if (sqlRows.next()) {
            return Mpa.builder().name(sqlRows.getString("rating_name"))
                    .id(sqlRows.getInt("rating_id")).build();
        } else {
            throw new NotFoundException("нет такого mpa");
        }
    }

    public List<Mpa> getAllRatings() {
        String sql = "select * from mpas";
        List<Mpa> mpas = jdbcTemplate.query(sql, (sqlRows, rowNum) -> Mpa.builder()
                .name(sqlRows.getString("rating_name"))
                .id(sqlRows.getInt("rating_id")).build());
        return mpas;
    }
}
