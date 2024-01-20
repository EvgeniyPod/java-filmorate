package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int generatorId = 1;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        if (user != null) {
            if (user.getEmail().isEmpty()) {
                log.error("Отсутствует адрес электронной почты");
                throw new ValidationException("Адрес электронной почты не может быть пустым");
            } else if (!user.getEmail().contains("@")) {
                log.error("Отсутствует символ: '@'");
                throw new ValidationException("В адресе электронной почты необходим символ '@'");
            }

            if (user.getLogin().isEmpty()) {
                log.error("Отсутсвует логин");
                throw new ValidationException("Логин не может быть пустым");
            } else if (user.getLogin().contains(" ")) {
                log.error("В логине присутствуют пробелы");
                throw new ValidationException("В логине не должны быть пробелы");
            }

            try {
                if (user.getName().isEmpty()) {
                    log.error("При создании не указано имя пользователя");
                    user.setName(user.getLogin());
                }
            } catch (NullPointerException nullPointerException) {
                log.error("При создании не указано имя пользователя");
                user.setName(user.getLogin());
            }

            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("День рождения пользователя указан в будущем");
                throw new ValidationException("День рождения пользователя не может быть в будущем");
            }
            user.setId(generatorId++);
            users.put(user.getId(), user);
        } else {
            log.error("Передан пустой объект");
            throw new ValidationException("Объект не может быть пустым");
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (user != null) {
            User updateUser = users.get(user.getId());

            if (updateUser == null) {
                log.error("Пользователь с id {} не найден", user.getId());
                throw new ValidationException("Пользователь не найден");
            }

            if (user.getEmail().isEmpty()) {
                log.error("Отсутствует адрес электронной почты");
                throw new ValidationException("Адрес электронной почты не может быть пустым");
            } else if (!user.getEmail().contains("@")) {
                log.error("Отсутствует символ: '@'");
                throw new ValidationException("В адресе электронной почты необходим символ '@'");
            }

            if (user.getLogin().isEmpty()) {
                log.error("Отсутсвует логин");
                throw new ValidationException("Логин не может быть пустым");
            } else if (user.getLogin().contains(" ")) {
                log.error("В логине присутствуют пробелы");
                throw new ValidationException("В логино не должны быть пробелы");
            }

            try {
                if (user.getName().isEmpty()) {
                    log.error("При создании не указано имя пользователя");
                    user.setName(user.getLogin());
                }
            } catch (NullPointerException nullPointerException) {
                log.error("При создании не указано имя пользователя");
                user.setName(user.getLogin());
            }

            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("День рождения пользователя указан в будущем");
                throw new ValidationException("День рождения пользователя не может быть в будущем");
            }
            updateUser.setEmail(user.getEmail());
            updateUser.setLogin(user.getLogin());
            updateUser.setName(user.getName());
            updateUser.setBirthday(user.getBirthday());
            users.put(user.getId(), updateUser);
        } else {
            log.error("Передан пустой объект");
            throw new ValidationException("Объект не может быть пустым");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
