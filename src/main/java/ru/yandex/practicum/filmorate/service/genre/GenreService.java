package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public List<Genre> getGenres() {
        return genreDao.getAllGenres();
    }

    public Genre getGenreById(int id) {
        if (genreDao.getGenreById(id) == null) {
            throw new ObjectIsNull("Жанр с id = " + id + " не найден.");
        }
        return genreDao.getGenreById(id);
    }

}