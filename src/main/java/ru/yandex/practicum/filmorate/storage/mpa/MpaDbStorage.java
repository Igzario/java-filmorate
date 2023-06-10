package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Data
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpa(int id) throws EntityNotFoundException {
        Mpa mpa;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select NAME from MPA WHERE MPA_ID = ?", id);
        if (userRows.next()) {
            String nameMpa = userRows.getString("NAME");
            mpa = new Mpa(id, nameMpa);
        } else {
            throw new EntityNotFoundException("MPA");
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() throws EntityNotFoundException {
        List<Mpa> mpaList = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select MPA_ID from MPA GROUP BY MPA_ID");
        if (userRows.next()) {
            userRows.previous();
            while (userRows.next()) {
                int mpaId = Integer.parseInt(Objects.requireNonNull(userRows.getString("MPA_ID")));
                mpaList.add(getMpa(mpaId));
            }
        } else {
            throw new EntityNotFoundException("MPA");
        }
        return mpaList;
    }
}