package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;

    @Override
    public Film addFilm(@RequestBody Film film) {
        if (film != null) {
            validate(film);
            if (film.getLikes() == null) {
                Set<Integer> usersWhoLikeFilm = new HashSet<>();
                film.setLikes(usersWhoLikeFilm);
            }
            film.setId(generatorId++);
            films.put(film.getId(), film);
        } else {
            log.error("Передан пустой объект");
            throw new NullPointerException("Объект не может быть пустым");
        }
        return film;
    }

    @Override
    public Film updateFilm(@RequestBody Film film) {
        if (film != null && films.containsKey(film.getId())) {
            validate(film);
            Film updateFilm = films.get(film.getId());
            updateFilm.setName(film.getName());
            updateFilm.setDescription(film.getDescription());
            updateFilm.setReleaseDate(film.getReleaseDate());
            updateFilm.setDuration(film.getDuration());
            films.put(film.getId(), updateFilm);
        } else {
            log.error("Передан пустой объект");
            throw new NullPointerException("Объект не может быть пустым");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(@PathVariable int id) {
        if (!films.containsKey(id)) {
            throw new ObjectIsNull("Фильма с таким номером не сущесвует");
        }
        return films.get(id);
    }

    @Override
    public boolean checkForAvailability(int id) {
        return films.containsKey(id);
    }

    @Override
    public List<Film> getBestFilms(int count) {
        return films.values().stream()
                .sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(int id, int userId) {
        if (!checkForAvailability(id)) {
            throw new ObjectIsNull("film с id = " + id + " не найден");
        }
        Film film = getFilmById(id);
        film.getLikes().add(userId);
        return film;
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
