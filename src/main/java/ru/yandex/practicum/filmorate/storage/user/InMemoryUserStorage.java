package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private int generatorId = 1;

    @Override
    public User addUser(User user) {
        validate(user);
        user.setId(generatorId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        User updateUser = users.get(user.getId());
        if (updateUser == null) {
            log.error("Пользователь с id {} не найден", user.getId());
            throw new ValidationException("Пользователь не найден");
        }

        updateUser.setEmail(user.getEmail());
        updateUser.setLogin(user.getLogin());
        updateUser.setName(user.getName());
        updateUser.setBirthday(user.getBirthday());
        users.put(user.getId(), updateUser);

        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    private void validate(User user) {
        if (user == null) {
            log.error("Передан пустой объект");
            throw new ValidationException("Объект не может быть пустым");
        }

        if (StringUtils.isEmpty(user.getEmail())) {
            log.error("Отсутствует адрес электронной почты");
            throw new ValidationException("Адрес электронной почты не может быть пустым");
        } else if (!user.getEmail().contains("@")) {
            log.error("Отсутствует символ: '@'");
            throw new ValidationException("В адресе электронной почты необходим символ '@'");
        }

        if (StringUtils.isEmpty(user.getLogin())) {
            log.error("Отсутсвует логин");
            throw new ValidationException("Логин не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            log.error("В логине присутствуют пробелы");
            throw new ValidationException("В логине не должны быть пробелы");
        }

        if (StringUtils.isEmpty(user.getName())) {
            log.error("При создании не указано имя пользователя");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("День рождения пользователя указан в будущем");
            throw new ValidationException("День рождения пользователя не может быть в будущем");
        }
    }
}
