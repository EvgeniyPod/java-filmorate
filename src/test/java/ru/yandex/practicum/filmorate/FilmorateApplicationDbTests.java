package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationDbTests {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void addNewFilm() {
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169, new Mpa(1, "G"), new ArrayList<>());
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.addFilm(film);
        Assertions.assertEquals(1, filmDbStorage.getFilms().size(), "Количество фильмов должно быть 1!");
        assertThat(film)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmDbStorage.getFilmById(1));
    }

    @Test
    void getFilmById() {
        Film film = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169, new Mpa(1, "G"), new ArrayList<>());
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.addFilm(film);
        Film neededFilm = filmDbStorage.getFilmById(film.getId());
        assertThat(film)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void getAllFilms() {
        Film film1 = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169, new Mpa(1, "G"), new ArrayList<>());
        Film film2 = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169, new Mpa(1, "G"), new ArrayList<>());
        Film film3 = new Film(1, "Интерстеллар", "Про космос",
                LocalDate.of(2014, 11, 6), 169, new Mpa(1, "G"), new ArrayList<>());
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);
        filmDbStorage.addFilm(film3);
        List<Film> films = List.of(film1, film2, film3);
        assertThat(films)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmDbStorage.getFilms());
    }

    @Test
    public void addNewUser() {
        User user = new User(1, "evgeniy@yandex.ru", "Evgeniy", "evgen",
                LocalDate.of(1999, 12, 18));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(user);
        assertThat(user)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userStorage.getUserById(user.getId()));
    }

    @Test
    public void findUserById() {
        User user = new User(1, "evgeniy@yandex.ru", "Evgeniy", "evgen",
                LocalDate.of(1999, 12, 18));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(user);
        User savedUser = userStorage.getUserById(user.getId());
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void getAllUsers() {
        User user1 = new User(1, "evgeniy@yandex.ru", "Evgeniy", "evgen",
                LocalDate.of(1999, 12, 18));
        User user2 = new User(2, "alexey@yandex.ru", "Alexey", "alex",
                LocalDate.of(1999, 5, 21));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        List<User> userList = List.of(user1, user2);
        assertThat(userList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userStorage.getUsers());
    }

    @Test
    public void addFriends() {
        User user1 = new User(1, "evgeniy@yandex.ru", "Evgeniy", "evgen",
                LocalDate.of(1999, 12, 18));
        User user2 = new User(2, "alexey@yandex.ru", "Alexey", "alex",
                LocalDate.of(1999, 5, 21));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        User user = userStorage.addFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(1, user.getFriends().size(), "Кол-во друзей должно быть 1.");
    }

    @Test
    public void deleteFriends() {
        User user1 = new User(1, "evgeniy@yandex.ru", "Evgeniy", "evgen",
                LocalDate.of(1999, 12, 18));
        User user2 = new User(2, "alexey@yandex.ru", "Alexey", "alex",
                LocalDate.of(1999, 5, 21));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        User user = userStorage.addFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(1, user.getFriends().size(), "Кол-во друзей должно быть 1.");
        User alongUser = userStorage.deleteFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(0, alongUser.getFriends().size(), "Кол-во друзей должно быть 0 после удаления.");
    }
}
