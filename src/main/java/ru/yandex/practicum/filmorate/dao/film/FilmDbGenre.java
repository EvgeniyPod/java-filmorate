package ru.yandex.practicum.filmorate.dao.film;

import lombok.Getter;

@Getter
public class FilmDbGenre {
    private int filmId;
    private Integer genreId;

    public FilmDbGenre(int filmId, Integer genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }

}
