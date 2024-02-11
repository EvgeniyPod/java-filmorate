package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> friendsOfUser = user.getFriends();
        Set<Integer> friendsOfFriend = friend.getFriends();
        friendsOfUser.add(friendId);
        friendsOfFriend.add(id);
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User exfriend = userStorage.getUserById(friendId);
        Set<Integer> friendsOfUser = user.getFriends();
        Set<Integer> friendsOfFriend = exfriend.getFriends();
        if (friendsOfUser.contains(friendId) && friendsOfFriend.contains(id)) {
            friendsOfUser.remove(friendId);
            friendsOfFriend.remove(id);
        }
        return user;
    }

    public List<User> getCommonFriends(int id, int otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherUserId);
        Set<Integer> friendsOfUser = new HashSet<>(user.getFriends());
        Set<Integer> friendsOfOtherUser = otherUser.getFriends();
        friendsOfUser.retainAll(friendsOfOtherUser);
        for (int commonFriend : friendsOfUser) {
            commonFriends.add(userStorage.getUserById(commonFriend));
        }
        return commonFriends;
    }

    public List<User> getFriendsOfUser(int id) {
        User user = userStorage.getUserById(id);
        List<User> friendsOfUser = new ArrayList<>();
        if (!user.getFriends().isEmpty()) {
            Set<Integer> friendsUser = user.getFriends();
            for (int friends : friendsUser) {
                friendsOfUser.add(userStorage.getUserById(friends));
            }
        }
        return friendsOfUser;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }
}
