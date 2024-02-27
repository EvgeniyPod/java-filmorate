package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.ObjectIsNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from category where category_id = ?", id);
        if (userRows.next()) {
            Mpa mpa = new Mpa(
                    userRows.getInt("category_id"),
                    userRows.getString("category_name")
            );
            log.info("Найден рейтинг (категория) фильма: {} {}", mpa.getId(), mpa.getName());
            return mpa;
        } else {
            log.info("Рейтинг (категория) фильма с идентификатором {} не найден.", id);
            throw new ObjectIsNull("Рейтин с id =" + id + "не найден.");
        }
    }

    @Override
    public List<Mpa> getMpa() {
        String sql = "select * from category";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("category_id"), rs.getString("category_name"));
    }
}
