package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;

    @Override
    public Film addFilm(Film film) {
        validate(film);
        film.setId(generatorId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        Film updateFilm = films.get(film.getId());

        if (updateFilm == null) {
            log.error("Фильм с id {} не найден", film.getId());
            throw new ValidationException("Пользователь не найден");
        }

        updateFilm.setName(film.getName());
        updateFilm.setDescription(film.getDescription());
        updateFilm.setReleaseDate(film.getReleaseDate());
        updateFilm.setDuration(film.getDuration());
        films.put(film.getId(), updateFilm);

        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    private void validate(Film film) {
        if (film == null) {
            log.error("Передан пустой объект");
            throw new ValidationException("Объект не может быть пустым");
        }

        if (StringUtils.isEmpty(film.getName())) {
            log.error("Фильм не имеет названия");
            throw new ValidationException("Фильм должен иметь название");
        }

        if (film.getDescription().length() > 200) {
            log.error("Описание фильма превышает лимит в 200 символов");
            throw new ValidationException("Описание фильма не должно превышать более 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Релиз фильма состоялся раньше 28 декабря 1895 года");
            throw new ValidationException("Релиз фильма не мог состояться раньше 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма отрицательное число");
            throw new ValidationException("Продолжительность фильма не может быть отрицательным числом");
        }
    }
}
