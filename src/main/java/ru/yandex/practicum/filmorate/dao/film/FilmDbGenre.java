package ru.yandex.practicum.filmorate.dao.film;

import lombok.Getter;

@Getter
public class FilmDbGenre {
    private int filmId;
    private int genreId;

    public FilmDbGenre(int filmId, int genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }

}
