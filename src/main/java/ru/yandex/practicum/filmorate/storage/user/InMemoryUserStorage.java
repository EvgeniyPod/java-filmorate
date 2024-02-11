package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private int generatorId = 1;

    @Override
    public User addUser(@RequestBody User user) {
        if (user != null) {
            validate(user);
            if (user.getFriends() == null) {
                Set<Integer> friendsOfUser = new HashSet<>();
                user.setFriends(friendsOfUser);
            }
            user.setId(generatorId++);
            users.put(user.getId(), user);
        } else {
            log.error("Передан пустой объект.");
            throw new NullPointerException("Объект не может быть пустым");
        }
        return user;
    }

    @Override
    public User updateUser(@RequestBody User user) {
        if (user != null) {
            User chosenUser = users.get(user.getId());
            validate(user);
            chosenUser.setName(user.getName());
            chosenUser.setEmail(user.getEmail());
            chosenUser.setLogin(user.getLogin());
            chosenUser.setBirthday(user.getBirthday());
            users.put(user.getId(), chosenUser);
        } else {
            log.error("Передан пустой объект.");
            throw new NullPointerException("Объект не может быть пустым");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(@PathVariable int id) {
        if (!users.containsKey(id)) {
            throw new ObjectIsNull("Внимание пользователя с таким номером не существует!");
        }
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
