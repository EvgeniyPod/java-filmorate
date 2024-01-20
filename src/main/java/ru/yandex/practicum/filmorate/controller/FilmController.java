package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        if(film != null) {
            if (film.getName().isEmpty()) {
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
            film.setId(generatorId++);
            films.put(film.getId(), film);
        } else {
            log.error("Передан пустой объект");
            throw new ValidationException("Объект не может быть пустым");
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if(film != null && films.containsKey(film.getId())) {
            Film updateFilm = films.get(film.getId());
            if (film.getName().isEmpty()) {
                log.error("Фильм не имеет названия");
                throw new ValidationException("Фильм должен иметь название");
            }

            if (film.getDescription().length() > 200) {
                log.error("Описание фильма превышет лимит в 200 символов");
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
            updateFilm.setName(film.getName());
            updateFilm.setDescription(film.getDescription());
            updateFilm.setReleaseDate(film.getReleaseDate());
            updateFilm.setDuration(film.getDuration());
            films.put(film.getId(), updateFilm);
        } else {
            log.error("Передан пустой объект");
            throw new ValidationException("Объект не может быть пустым");
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
