package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(int id);

    boolean checkForAvailability(int id);

    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);

    List<User> getUsersFriends(int id);

    List<User> getCommonFriends(int id, int otherId);
}
