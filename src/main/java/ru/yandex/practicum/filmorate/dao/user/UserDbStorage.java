package ru.yandex.practicum.filmorate.dao.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Primary
@Component
@Slf4j
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(userId);
        return getUserById(userId);
    }

    @Override
    public User updateUser(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sql = "update users set name = ?, email = ?, login = ?, birthday = ? where user_id = ? ";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public List<User> getUsers() {
        String sql = "select distinct u.user_id, u.name , u.email , u.login , u.birthday ," +
                " uf.friend_id from users u left join user_friends uf on u.user_id = uf.user_id";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getId(), user);
        }
        List<UserDbFriends> userDbFriends = jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserDbFriends(rs.getInt("user_id"), rs.getInt("friend_id")));
        Map<Integer, Set<Integer>> userFriends = new HashMap<>();
        for (UserDbFriends userDbFriend : userDbFriends) {
            userFriends.put(userDbFriend.getUserId(), new HashSet<>());
            if (userFriends.containsKey(userDbFriend.getUserId()) && userDbFriend.getFriendId() != 0) {
                userFriends.get(userDbFriend.getUserId()).add(userDbFriend.getFriendId());
            }
        }
        List<User> users1 = new ArrayList<>(userMap.values());
        users1.forEach(u -> u.setFriends(userFriends.get(u.getId())));
        return users1;
    }

    @Override
    public User getUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate()
            );
            user.setFriends(new HashSet<>(findFriendsByUserId(id)));
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectIsNull("Пользователь с id = " + id + " не найден.");
        }
    }

    @Override
    public boolean checkForAvailability(int id) {
        return getUserById(id) != null;
    }

    @Override
    public User addFriend(int id, int friendId) {
        String sql = "insert into user_friends values (?, ?, false)";
        jdbcTemplate.update(sql, id, friendId);
        return getUserById(id);
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        String sql = "delete from user_friends where user_id  = ? and friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
        return getUserById(id);
    }

    @Override
    public List<User> getUsersFriends(int id) {
        String sql = "select * from users where user_id in (select friend_id from user_friends where user_id" +
                " = ? order by friend_id)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        checkForAvailability(id);
        checkForAvailability(otherId);
        String sql = "select * from users \n" +
                "where user_id = \n" +
                "(select  uf.friend_id from \n" +
                "(select friend_id from user_friends where user_id = ?) as tt \n" +
                "inner  join (select friend_id from user_friends where user_id = ?) uf on uf.friend_id = tt.friend_id)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, otherId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("login"),
                Objects.requireNonNull(rs.getDate("birthday")).toLocalDate()
        );
    }

    private List<Integer> findFriendsByUserId(int userId) {
        String sql = "select friend_id from user_friends where user_id = ? order by friend_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"), userId);
    }
}
